package pt.isel

import java.net.URI
import kotlin.coroutines.Continuation
import kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED
import kotlin.coroutines.resume

fun fetch(path: String): String {
    println("Fetching $path")
    return URI(path).toURL().readText()
}


fun fetchCps(url: String, onComplete: Continuation<String>): Any {
    println("Fetching...")
    // DO NOT DO this in productioncode
    // thread {
    val body = URI(url).toURL().readText()
    onComplete.resume(body)
    // }
    return COROUTINE_SUSPENDED
}