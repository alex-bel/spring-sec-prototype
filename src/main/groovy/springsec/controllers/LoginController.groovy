package springsec.controllers

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter
import org.springframework.security.web.authentication.switchuser.SwitchUserGrantedAuthority
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import springsec.DataStorage
import springsec.sec.AuthTokenManager
import springsec.sec.model.AppPrincipal
import springsec.sec.model.CustomUsernamePasswordAuthenticationRequest

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@CompileStatic @TypeChecked
@Controller
class LoginController {

    @Autowired
    AuthenticationManager authenticationManager

    @Autowired
    DataStorage dataStorage

    @Autowired
    AuthTokenManager authTokenManager

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    String login(HttpServletRequest req, @RequestParam login, @RequestParam password, @RequestParam String localeId) {
        Locale locale = new Locale.Builder().setLanguageTag(localeId).build()
        UsernamePasswordAuthenticationToken token = new CustomUsernamePasswordAuthenticationRequest(login, password)
        token.localeId = localeId

        def res = authenticationManager.authenticate(token)
        req.getSession(true)
        SecurityContextHolder.context.authentication = res
        return "OK!"
    }

    @RequestMapping(value = "/loginInfo", method = RequestMethod.GET)
    @ResponseBody
    String loginInfo(HttpServletRequest req, HttpServletResponse resp) {
        StringBuilder sb = new StringBuilder()

        def reqPrincipal = req.userPrincipal
        sb << "Request principal: " << reqPrincipal << "\n"

        def springPrincipal = SecurityContextHolder.context?.authentication
        sb << "SpringPrincipal principal: " << springPrincipal << "\n"

        return sb.toString().replace("\n", "<br>")
    }

    @RequestMapping(value = "/impersonalize", method = RequestMethod.GET)
    @ResponseBody
    String impersonalize(HttpServletRequest req, HttpServletResponse resp, @RequestParam String login) {
        def currentAuth = SecurityContextHolder.context?.authentication as AppPrincipal
        if (!currentAuth) {
            return "Not authenticated"
        }

        def newUser = dataStorage.getUserByLogin(login);
        if (!newUser) {
            return "User not found";
        }


        List<GrantedAuthority> authorities = []
        authorities.addAll(AuthorityUtils.createAuthorityList(newUser.role))
        authorities << new SwitchUserGrantedAuthority(SwitchUserFilter.ROLE_PREVIOUS_ADMINISTRATOR, currentAuth)

        AppPrincipal newPrincipal = new AppPrincipal(authorities)
        newPrincipal.fullUserName = newUser.name
        newPrincipal.userId = newUser.id
        newPrincipal.localeId = currentAuth.localeId
        newPrincipal.userName = login
        newPrincipal.authenticated = true

        SecurityContextHolder.context.authentication = newPrincipal
        return "OK, logged in as ${login}"
    }

    @RequestMapping(value = "/deImpersonalize", method = RequestMethod.GET)
    @ResponseBody
    String deImpersonalize(HttpServletRequest req, HttpServletResponse resp) {
        def currentAuth = SecurityContextHolder.context?.authentication as AppPrincipal
        if (!currentAuth) {
            return "Not authenticated"
        }

        def switchUserAuthority = currentAuth.authorities.find { a -> a instanceof SwitchUserGrantedAuthority } as SwitchUserGrantedAuthority
        if (!switchUserAuthority) {
            return "Not impersonalized"
        }

        def oldPrincipal = switchUserAuthority.source as AppPrincipal
        SecurityContextHolder.context.authentication = oldPrincipal
        return "OK, returned as ${oldPrincipal.userName}"
    }

    @RequestMapping(value = "/loginToken", method = RequestMethod.GET)
    @ResponseBody
    String loginToken(HttpServletRequest req, @RequestParam login, @RequestParam password, @RequestParam String localeId) {
        Locale locale = new Locale.Builder().setLanguageTag(localeId).build()
        UsernamePasswordAuthenticationToken token = new CustomUsernamePasswordAuthenticationRequest(login, password)
        token.localeId = localeId

        AppPrincipal principal = authenticationManager.authenticate(token) as AppPrincipal
        def t = authTokenManager.createTokenForPrincipal(principal)

        return "OK, your token is ${t}"
    }


    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    String logout(HttpServletRequest req) {
        def currentAuth = SecurityContextHolder.context?.authentication as AppPrincipal
        if (!currentAuth) {
            return "Not authenticated"
        }

        SecurityContextHolder.context.authentication = null
        if (currentAuth.token) {
            authTokenManager.removeToken(currentAuth.token)
        }

        return "OK!"
    }
}
