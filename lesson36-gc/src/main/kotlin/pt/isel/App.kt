package pt.isel

fun main() {
    checkGarbage()
    checkGarbageWithRootReference()
    println("Try clean some garbage after checkGarbage() finish!")
    System.gc()
    printAllocatedMemory()
}
// var rr: Any? = null
fun checkGarbageWithRootReference() {
    println("Initial JVM mem")
    printAllocatedMemory()
    /**
     * Free memory cleaning unreachable objects!!!
     * !!! Use it careful  !!!
     */
    println("After clean")
    System.gc()
    printAllocatedMemory()
    println("After allocate some garbage and store in Root Reference")
    val rr = makeGarbage()
    printAllocatedMemory()
    println("Try clean some garbage")
    System.gc()
    printAllocatedMemory()
}

fun checkGarbage() {
    println("Initial JVM mem")
    printAllocatedMemory()
    /**
     * Free memory cleaning unreachable objects!!!
     * !!! Use it careful  !!!
     */
    println("After clean")
    System.gc()
    printAllocatedMemory()
    println("After allocate some garbage")
    makeGarbage()
    printAllocatedMemory()
    println("After clean some garbage")
    System.gc()
    printAllocatedMemory()
}

const val size = 100_000

fun makeGarbage() : Any{
    val arr = arrayOfNulls<Any>(size)
    for(i in 0 until size) {
        arr[i] = Any()
    }
    return arr
}

val runtime = Runtime.getRuntime()

fun printAllocatedMemory() {
    // Value in Bytes
    val memAllocated = runtime.totalMemory() - runtime.freeMemory()
    println("MEM = ${memAllocated/1024} Kb")
}