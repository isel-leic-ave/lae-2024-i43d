package pt.isel

import kotlinx.coroutines.future.await
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

private val httpClient: HttpClient = HttpClient.newHttpClient()
private val requestBuilder = HttpRequest.newBuilder()
private fun request(url: String) = requestBuilder.uri(URI.create(url)).build()


suspend fun fetchSuspend(url: String): String {
    println("Fetching $url")
    return httpClient
        .sendAsync(request(url), HttpResponse.BodyHandlers.ofString())
        .thenApply(HttpResponse<String>::body)
        .await()
}
