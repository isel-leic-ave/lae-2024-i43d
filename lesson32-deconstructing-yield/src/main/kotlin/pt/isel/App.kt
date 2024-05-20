package pt.isel

import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext

fun fooSequence() : Sequence<String> {
    return sequence {
        // val cont = object: Continuation<String> { resumeWith(res: String) {...} }
        // yield("first", cont)
        // yield("second", cont)
        // ...
        // <=>
        yield("first")
        yield("second")
        yield("third")
    }
}

fun foo() {
    // pause
    println("first")
    // pause
    println("second")
    // pause
    println("third")
}

interface Advancer { fun resume() }

fun suspendFoo(): FooStateMachine = FooStateMachine()

class FooStateMachine : Advancer, Continuation<Unit> {
    var state = 0

    override fun resume() {
        when (state) {
            0 -> println("first")
            1 -> println("second")
            2 -> println("third")
        }
        state++
    }

    override val context = EmptyCoroutineContext

    override fun resumeWith(result: Result<Unit>) {
        resume()
    }
}

fun main() {
    val adv = suspendFoo()
    adv.resume()
    adv.resume()
}