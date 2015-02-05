package springsec.model

import groovy.transform.CompileStatic
import groovy.transform.ToString
import groovy.transform.TypeChecked

@CompileStatic @TypeChecked
@ToString(includeNames = true)
class Account {
    Long id
    String name
}
