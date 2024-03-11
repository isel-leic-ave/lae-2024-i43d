object B {
    fun m() {
        println("I am B")
    }
}
fun main() {
    // B() // Not possible because B is a singleton 
    B.m()
}