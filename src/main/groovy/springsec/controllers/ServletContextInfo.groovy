package springsec.controllers

import javax.servlet.http.HttpSessionEvent
import javax.servlet.http.HttpSessionListener

/**
 * Just listens for 'session created' and 'session destroyed' events
 * And prints info in console when these events happen
 */
class ServletContextInfo implements HttpSessionListener {
    @Override
    void sessionCreated(HttpSessionEvent se) {
        println "sessionCreated"
    }

    @Override
    void sessionDestroyed(HttpSessionEvent se) {
        println "sessionDestroyed"
    }
}
