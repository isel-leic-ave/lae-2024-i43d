fun main() {
    val b = B()
    b.print()
    val a: A = b
    a.print()
}

open class A {
    open fun print() {
        println("I am A")
    }
}

class B : A() {
    override fun print() {
        println("I am B")
    }
}