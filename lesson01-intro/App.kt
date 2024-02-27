class Student(var name: String?)

fun main() {
    println("Hello")
    val a: Int = 7
    var b = a      // copy value of 'a' to 'b'
    b = 9          // change variable 'b' and keeps the former value in variable 'a'
    println(a) // 7 
    println(b) // 9 
    val x = Student("Jose")
    val y = x         // copy reference
    y.name = "Maria"  // change the name in referred object
    /*
     * Variables x and y point to the same object. 
     */
    println(x.name) // Maria
    println(y.name) // Maria
    

    foo("Jose Manel")
}

fun foo(name: String) {
    require(name.length < 40)
}