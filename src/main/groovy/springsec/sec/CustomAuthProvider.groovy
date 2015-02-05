package springsec.sec

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import springsec.sec.model.AppPrincipal
import springsec.sec.model.AuthUserDetails
import springsec.sec.model.CustomUsernamePasswordAuthenticationRequest

/**
 * Spring authentication provider that is plugged into Spring security's AuthenticationManager
 * Handles Principal creation in case of successful authentication
 */
@CompileStatic @TypeChecked
class CustomAuthProvider extends DaoAuthenticationProvider {

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        AuthUserDetails userDetails = (AuthUserDetails) user
        CustomUsernamePasswordAuthenticationRequest auth = (CustomUsernamePasswordAuthenticationRequest) authentication

        AppPrincipal res = new AppPrincipal(user.getAuthorities())
        res.fullUserName = userDetails.name
        res.userId = userDetails.userId
        res.localeId = auth.localeId
        res.userName = userDetails.username
        res.authenticated = true

        return res
    }
}
