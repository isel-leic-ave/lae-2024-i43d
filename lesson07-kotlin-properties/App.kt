class Person(var name: String, val nr: Int)

fun main() {
    val p = Person("Maria", 7234568)
    println(p.name)
    p.name = "Maria Silva"
    println(p.name)
}