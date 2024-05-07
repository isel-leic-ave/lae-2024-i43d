package pt.isel

import pt.isel.FilterIteratorState.*
import java.util.NoSuchElementException

fun <T, R> Iterable<T>.eagerMap(transform: (T) -> R) : Iterable<R> {
    val destination = mutableListOf<R>()
    for (item in this)
        destination.add(transform(item))
    return destination
}

fun <T> Iterable<T>.eagerFilter(predicate: (T) -> Boolean) : Iterable<T> {
    val destination = mutableListOf<T>()
    for (item in this)
        if(predicate(item))
            destination.add(item)
    return destination
}

fun <T, R> Sequence<T>.lazyMap(transform: (T) -> R) : Sequence<R> {
    return  object : Sequence<R> {
        override fun iterator(): Iterator<R> {
            return object : Iterator<R> {
                val iter = this@lazyMap.iterator()
                override fun hasNext() = iter.hasNext()
                override fun next() = transform(iter.next())
            }
        }
    }
}

enum class FilterIteratorState {NotReady, Ready, Finish}

fun <T : Any> Sequence<T>.lazyFilter(predicate: (T) -> Boolean) : Sequence<T> {
    return object : Sequence<T> {
        override fun iterator(): Iterator<T> {
            return object : Iterator<T> {
                var state = NotReady
                val iter = this@lazyFilter.iterator()
                lateinit var nextItem: T

                /**
                 * It advances if iter hasNext() is true and if the
                 * next element satisfies the predicate.
                 * It only tries to advance if it is NotReady.
                 * After trying to advance it will stay Ready or Finish (after reaching the last element)
                 */
                private fun tryAdvance() {
                    if(state == NotReady) {
                        while(iter.hasNext()) {
                            val curr = iter.next()
                            if(predicate(curr)) {
                                nextItem = curr
                                state = Ready
                                return
                            }
                        }
                        state = Finish
                    }
                }

                override fun hasNext(): Boolean {
                    tryAdvance()
                    return state == Ready
                }

                override fun next(): T {
                    if(state == Finish || !hasNext()) throw NoSuchElementException()
                    state = NotReady
                    return nextItem
                }
            }
        }

    }
}

fun <T : Any> Sequence<T>.yieldFilter(predicate: (T) -> Boolean) = sequence {
    for (item in this@yieldFilter)
        if(predicate(item))
            yield(item)
}

/**
 * TPC
 */
enum class DistinctIteratorState {NotReady, Ready, Finish}

fun <T> Sequence<T>.lazyDistinct(): Sequence<T> {
    return object : Sequence<T> {
        override fun iterator(): Iterator<T> {
            return object : Iterator<T> {
                var state = DistinctIteratorState.NotReady
                val iter = this@lazyDistinct.iterator()
                val l = mutableSetOf<T>()

                private fun tryAdvance() {
                    if (state == DistinctIteratorState.NotReady) {
                        while (iter.hasNext()) {
                            val item = iter.next()
                            if (l.add(item)) {
                                state = DistinctIteratorState.Ready
                                return
                            }
                        }
                        state = DistinctIteratorState.Finish
                    }
                }
                override fun hasNext(): Boolean {
                    tryAdvance()
                    return state == DistinctIteratorState.Ready
                }

                override fun next(): T {
                    if (!hasNext()) throw NoSuchElementException()
                    state = DistinctIteratorState.NotReady
                    return l.last()
                }
            }
        }
    }
}

fun <T> Sequence<T>.yieldDistinct() = sequence {
    val consumedElement = mutableSetOf<T>()
    for (item in this@yieldDistinct)
        if (consumedElement.add(item)) {
            yield(item)
        }
}

fun <T> Sequence<T>.concat(other: Sequence<T>) : Sequence<T> {
    TODO()
}

fun <T> Sequence<T>.yieldConcat(other: Sequence<T>) : Sequence<T> {
    TODO()
}

/**
 * Merges series of adjacent elements
 */
fun <T> Sequence<T>.collapse() : Sequence<T> {
    TODO()
}
/**
 * Merges series of adjacent elements
 */
fun <T> Sequence<T>.yieldCollapse() : Sequence<T> {
    TODO()
}
