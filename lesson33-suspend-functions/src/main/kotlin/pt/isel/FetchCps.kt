package pt.isel

import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.resume

fun main() {
    fetchManyCps(
        "https://github.com",
        "https://stackoverflow.com/",
        "https://developer.mozilla.org",
        object: Continuation<List<String>>{
            override val context = EmptyCoroutineContext
            override fun resumeWith(result: Result<List<String>>) {
                result
                    .getOrThrow()
                    .map { it
                        .split("<title>")[1]
                        .split("</title")[0]
                    }
                    .forEach { println(it) }
            }
        })
}

fun fetchManyCps(url1: String, url2: String, url3: String, onComplete: Continuation<List<String>>) {
    FetchManyCps(url1, url2, url3, onComplete)
        .resumeWith(Result.success(null))
}

class FetchManyCps(
    val url1: String,
    val url2: String,
    val url3: String,
    val onComplete: Continuation<List<String>>
) : Continuation<String?> {
    val bodies = mutableListOf<String>()
    var label = 0

    override fun resumeWith(result: Result<String?>) {
        result.getOrThrow()?.let { bodies.add(it) }
        when(label++) {
            0 -> fetchCps(url1,this)
            1 -> fetchCps(url2,this)
            2 -> fetchCps(url3,this)
            3 -> onComplete.resume(bodies)
        }
    }

    override val context: CoroutineContext
        get() = TODO("Not yet implemented")

}