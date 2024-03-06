package pt.isel;

fun main() {
    val ca: A = object : A() {
        override fun foo() {
            println("I am ca")
        }
    }
    val cb: A = object : A() {
        override fun foo() {
            println("I am cb")
        }
    }
}