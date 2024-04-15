import kotlin.math.sqrt

fun modulus(x: Float, y: Float) : Float {
    val res = sqrt(x*x + y*y)
    return res
}

fun calculateNetBalance(
    balance: Int,
    tax: Float,
    interest: Float,
    income: Int,
    expense: Float
): Float {
    return balance - balance * tax + balance * interest + income - expense
}

class Person(val name: String) {
    fun print(label: String) {
        // this is local variable 0
        val myName = this.name
        // Label is local variable 1
        println(label + ": " + myName)
    }
}

class Student()

fun main() {
    val st = Student()
    println(st)
}