package pt.isel

import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.resume


interface Yieldable<T> {
    suspend fun yield(item: T)
}

fun main() {
    val seq: Sequence<String> = buildSequence<String> {
        yield("first")
        yield("second")
        yield("third")
    }
}

fun <T> buildSequence(block: suspend Yieldable<T>.() -> Unit): Sequence<T> {
    return object : Sequence<T> {
        override fun iterator(): Iterator<T> {
            return SequenceBuilderIterator<T>(block)
        }
    }
}

enum class SequenceState {Ready, NotReady, Done, Failure}

class SequenceBuilderIterator<T>(val block: suspend Yieldable<T>.() -> Unit) :
    Yieldable<T>,
    Iterator<T>,
    Continuation<Unit>
{
    var state = SequenceState.NotReady
    var nextItem: T? = null
    var nextStep: Continuation<Unit>? = null
    val blockCps = block as (Yieldable<T>.(Continuation<Unit>) -> Any)

    override fun hasNext(): Boolean {
        TODO("Not yet implemented")
    }

    override fun next(): T {
        // block()
        // Deliver the next item stored on yield
        nextStep
            ?.resume(Unit)
            ?: blockCps(onCompletion) // Calling a suspend function using CPS
        return nextItem as T
    }

    val onCompletion = object : Continuation<Unit> {
        override val context = EmptyCoroutineContext
        override fun resumeWith(result: Result<Unit>) {
            state = if(result.isSuccess) SequenceState.Done
            else SequenceState.Failure
        }
    }

    override val context: CoroutineContext
        get() = TODO("Not yet implemented")

    override fun resumeWith(result: Result<Unit>) {
        TODO("Not yet implemented")
    }

    override suspend fun yield(item: T) {
        yieldHandler(item)
    }

    val yieldHandler = ::onYield as (suspend (T)->Unit)

    fun onYield(item: T, cont: Continuation<Unit>) {
        nextItem = item
        nextStep = cont
    }
}