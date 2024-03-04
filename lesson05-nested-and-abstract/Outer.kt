class Outer(val name: String) {
    class Nested {}

    inner class Xpto() {
        fun print() {
            println("Outer name = $name")
        }
    }
}