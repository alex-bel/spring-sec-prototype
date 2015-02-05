package springsec.sec

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.stereotype.Component
import springsec.sec.model.AppPrincipal

import java.util.concurrent.TimeUnit

/**
 * Manager of auth tokens (basically, cache with some expiration time)
 */
@CompileStatic @TypeChecked
@Component
class AuthTokenManager {
    Cache<String, AppPrincipal> tokensCache = CacheBuilder.newBuilder()
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .maximumSize(20000)
            .build()

    String createTokenForPrincipal(AppPrincipal p) {
        String token = RandomStringUtils.random(10, true, true)
        p.token = token

        tokensCache.put(token, p)
        return token
    }

    AppPrincipal getPrincipalByToken(String t) {
        return tokensCache.getIfPresent(t)
    }

    void removeToken(String t) {
        tokensCache.invalidate(t)
    }
}
