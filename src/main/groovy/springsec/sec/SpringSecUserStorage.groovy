package springsec.sec

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import springsec.DataStorage
import springsec.sec.model.AuthUserDetails

/**
 * Adapter for Spring's Authentication provider
 * Fetches users from DB
 */
@CompileStatic @TypeChecked
class SpringSecUserStorage implements UserDetailsService {

    @Autowired
    DataStorage dataStorage

    @Override
    AuthUserDetails loadUserByUsername(String username) {
        def user = dataStorage.getUserByLogin(username)
        if (user == null) {
            throw new UsernameNotFoundException("User '${username}' not found in DB")
        }

        def res = new AuthUserDetails(username, user.password, true, true, true, true,
                AuthorityUtils.createAuthorityList(user.role)
        )

        res.name = user.name
        res.userId = user.id
        return res
    }
}
