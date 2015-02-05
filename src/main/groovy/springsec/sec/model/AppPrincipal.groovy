package springsec.sec.model

import groovy.transform.CompileStatic
import groovy.transform.ToString
import groovy.transform.TypeChecked
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

/**
 * Principal used in the application
 * Principal describes logged in user performing request
 */
@CompileStatic @TypeChecked
@ToString(includeSuper = true, includeNames = true)
class AppPrincipal extends AbstractAuthenticationToken {
    Long userId
    String userName
    String localeId
    String fullUserName

    String token

    // currently selected account id
    Long accountId

    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the
     *                    principal represented by this authentication object.
     */
    AppPrincipal(Collection<? extends GrantedAuthority> authorities) {
        super(authorities)
    }

    @Override
    Object getCredentials() {
        return null
    }

    @Override
    Object getPrincipal() {
        return userName
    }
}
