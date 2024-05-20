package pt.isel

import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext

fun main() {
    val res = fetchManyCpsLazy(
        "https://github.com",
        "https://stackoverflow.com/",
        "https://developer.mozilla.org"
    )
    val iter = res.iterator()
    iter.next().let { println(it
        .split("<title>")[1]
        .split("</title")[0]) }
    iter.next().let { println(it
        .split("<title>")[1]
        .split("</title")[0]) }
}

fun fetchManyCpsLazy(url1: String, url2: String, url3: String) : Sequence<String> {
    return object:Sequence<String> {
        override fun iterator(): Iterator<String> {
            return FetchManyCpsIterator(url1, url2, url3)
        }

    }
}

class FetchManyCpsIterator(
    val url1: String,
    val url2: String,
    val url3: String
) : Continuation<String>, Iterator<String> {
    var label = 0
    lateinit var nextItem: String

    override fun resumeWith(result: Result<String>) {
        nextItem = result.getOrThrow()
    }
    fun block() {
        when(label++) {
            0 -> fetchCps(url1,this)
            1 -> fetchCps(url2,this)
            2 -> fetchCps(url3,this)
            else -> throw NoSuchElementException()
        }
    }

    override val context = EmptyCoroutineContext
    override fun hasNext() = label < 3
    override fun next(): String {
        if(!hasNext()) throw NoSuchElementException()
        block()
        return nextItem
    }

}