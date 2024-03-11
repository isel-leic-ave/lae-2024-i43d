const val DEFAULT_TIMEOUT = 5000
const val APPLICATION_NAME = "MyApp"

fun Int.isEven():Boolean{
    return this % 2 == 0
}

fun main() {
    println("7 is even = " +  7.isEven())
    println("11 is Even = " + 12.isEven())
}