package pt.isel

import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.concurrent.thread
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED
import kotlin.coroutines.resume

private val httpClient: HttpClient = HttpClient.newHttpClient()
private val requestBuilder = HttpRequest.newBuilder()
private fun request(url: String) = requestBuilder.uri(URI.create(url)).build()


fun main() {
    val fetchCpsHandle = ::fetchSuspend as ((String, Continuation<String>) -> Unit)
    fetchCpsHandle("https://github.com", object: Continuation<String> {
        override val context = EmptyCoroutineContext
        override fun resumeWith(result: Result<String>) {
            val body = result.getOrThrow()
            body
                .split("<title>")[1]
                .split("</title")[0]
                .let { println(it) }
        }
    })
    Thread.sleep(2000)

    val fetchSuspendHandle = ::fetchCps as (suspend (String) -> String)
    runBlocking {
        val body = fetchSuspendHandle("https://github.com")
        body
            .split("<title>")[1]
            .split("</title")[0]
            .let { println(it) }
    }
}

suspend fun fetchSuspend(url: String): String {
    println("Fetching $url")
    return httpClient
        .sendAsync(request(url), HttpResponse.BodyHandlers.ofString())
        .thenApply(HttpResponse<String>::body)
        .await()
}

suspend fun test() {
    /*
     * <=> fetchCpsHandle("https://github.com", object: Continuation<String> {
        override val context = EmptyCoroutineContext
        override fun resumeWith(result: Result<String>) {
            val body = result.getOrThrow()
            body
     */
    val body = fetchSuspend("https://github.com")
    body
        .split("<title>")[1]
        .split("</title")[0]
        .let { println(it) }
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