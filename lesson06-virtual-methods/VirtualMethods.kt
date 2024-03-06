open class A {
    open fun v() {
        println("I am A");
    }
    /**
     * Implicitly final => not virtual
     */
    fun nv() {
        println("I am A");
    }
}
class B : A() {
    override fun v() {
        println("I am B");
    }
    /**
     * 'nv' hides member of supertype 'A'
     * 'nv' is not virtual (not open)
     */
    fun nv() {
        println("I am A");
    }
}