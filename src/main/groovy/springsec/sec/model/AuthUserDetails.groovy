package springsec.sec.model

import groovy.transform.CompileStatic
import groovy.transform.ToString
import groovy.transform.TypeChecked
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

/**
 * Information about user fetched from DB
 * Added name (full name) and userId fields because this info should be added to the Principal in case of successful authentication
 */
@CompileStatic @TypeChecked
@ToString(includeSuper = true, includeNames = true)
class AuthUserDetails extends User {
    String name
    Long userId

    AuthUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities)
    }
}
