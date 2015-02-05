package springsec.sec

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * Spring web filter that checks for 'authToken' param or cookie and, if it exists, authenticates client using this token
 * Must be placed in filter chain before authorization filter
 */
@CompileStatic @TypeChecked
class AuthTokenAuthenticationFilter extends GenericFilterBean {

    @Autowired
    AuthTokenManager authTokenManager

    @Override
    void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpReq = request as HttpServletRequest
        HttpServletResponse httpResp = response as HttpServletResponse

        String t = httpReq.getParameter('authToken')
        if (!t) {
            def cookie = httpReq.cookies?.find { c -> c.name == 'authToken' }
            if (cookie) {
                t = cookie.value
            }
        }

        if (t) {
            def principal = authTokenManager.getPrincipalByToken(t)
            if (!principal) {
                httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Failed: Invalid token");
                return
            }

            SecurityContextHolder.context.authentication = principal
        }

        chain.doFilter(request, response)
    }
}
