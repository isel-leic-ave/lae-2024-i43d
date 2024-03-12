package pt.isel

import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.memberProperties

/**
 * Represents the property Country in class Person
 */
val countryOfPerson: KProperty<*> = Person::class
    .memberProperties
    .first { it.name == "country" }

class Account(val country: String)

fun main() {
    // val klass: KClass<Person> = Person::class
    val maria = Person("Maria", "Portugal")
    val pedro = Person("Pedro", "Brasil")
    checkDeclaredMembers(maria)

    // ??? get the value of property country through countryProperty
    println(countryOfPerson.call(maria))
    println(countryOfPerson.call(pedro))

    val std = Student("Manuela", "Italy")
    /**
     * IllegalArgumentException: object is not an instance of declaring class
     */
    // println(countryOfPerson.call(std))
    checkCountry(std)
    checkCountry(Account("Spain"))
    checkCountry("ISEL")

}

fun checkCountry(obj: Any) {
    val propCountry = obj::class
        .memberProperties
        .first { it.name == "country" }
    println(propCountry.call(obj))
}

fun checkMembers(obj: Any) {
    val klass: KClass<*> = obj::class
    klass.members.forEach { println(it.name) }
}

fun checkDeclaredMembers(obj: Any) {
    val klass: KClass<*> = obj::class
    klass.declaredMembers.forEach { println(it.name) }
}