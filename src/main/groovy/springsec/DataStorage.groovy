package springsec

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import org.springframework.stereotype.Component
import springsec.model.Account
import springsec.model.User

/**
 * Some data storage (DB emulation)
 */
@Component
@CompileStatic @TypeChecked
class DataStorage {
    static Map<Long, User> users = [:]
    static Map<Long, Account> accounts = [:]

    static {
        [
                new User(id: 1L, name: 'Vasya Pupkin', password: '123', role: 'ROLE_USER'),
                new User(id: 2L, name: 'Ivan Ivanov', password: '123', role: 'ROLE_USER'),
                new User(id: 3L, name: 'Petr Petrov', password: '123', role: 'ROLE_MANAGER'),
                new User(id: 4L, name: 'Marina Marinova', password: '123', role: 'ROLE_MANAGER'),
        ].each { u -> users[u.id] = u }

        [
                new Account(id: 100L, name: 'Horns and Hoofs'),
                new Account(id: 101L, name: 'NanoChair LTD'),
        ].each { a -> accounts[a.id] = a }
    }

    Account getAccountById(long id) {
        return accounts[id]
    }

    User getUserById(long id) {
        return users[id]
    }

    User getUserByLogin(String login) {
        return users.values().find { u -> login == u.name.replace(' ', '_').toLowerCase() }
    }
}
