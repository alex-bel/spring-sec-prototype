package springsec.model

import groovy.transform.CompileStatic
import groovy.transform.ToString
import groovy.transform.TypeChecked

@CompileStatic @TypeChecked
@ToString(includeNames = true)
class User {
    Long id
    String name
    String password
    String role
}
