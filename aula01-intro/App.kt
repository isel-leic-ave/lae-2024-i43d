class Student(var name: String?)

fun main() {
    println("Hello")
    val a: Int = 7
    var b = a
    b = 9
    println(a) // 7 
    println(b) // 9 
    val x = Student("Jose")
    val y = x
    y.name = "Maria"
    println(x.name) // Maria
    println(y.name) // Maria
    

    // foo("Jose Manel")
}

fun foo(name: String) {
    require(name.length < 40)
}