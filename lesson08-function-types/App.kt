fun main() {
    var f: (String) -> Int
    f = { str -> str.length }
    println(f("Ola ISEL"))

    var bar = { s: String, ch: Char -> s + ch }
}