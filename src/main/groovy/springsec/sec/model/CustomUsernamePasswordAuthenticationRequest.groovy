package springsec.sec.model

import groovy.transform.CompileStatic
import groovy.transform.ToString
import groovy.transform.TypeChecked
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority

/**
 * Extended UsernamePasswordAuthenticationToken
 * Added localeId field because it should also be added to the Principal in case iof successfull authentication
 */
@CompileStatic @TypeChecked
@ToString(includeSuper = true, includeNames = true)
class CustomUsernamePasswordAuthenticationRequest extends UsernamePasswordAuthenticationToken {

    String localeId

    CustomUsernamePasswordAuthenticationRequest(Object principal, Object credentials) {
        super(principal, credentials)
    }

    CustomUsernamePasswordAuthenticationRequest(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities)
    }
}
