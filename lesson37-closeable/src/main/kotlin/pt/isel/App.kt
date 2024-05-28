package pt.isel

import java.io.File
import java.io.FileOutputStream

fun main() {
    val w1 = FileOutputStream("ole.txt")
        .also { it.channel.lock() }
        .bufferedWriter()
    val w2 = FileOutputStream("ole.txt")
        .also { it.channel.lock() }
        .bufferedWriter()
    w1.write("Meu 1")
    w1.flush()
    w2.write("Meu 2")
    w2.flush()
    w1.write("Super 1")
    w1.flush()
    w2.write("Super 2")
    w2.flush()
}