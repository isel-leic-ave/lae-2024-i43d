
fun incSeven(n: Int?) : Int? {
    require(n != null)
    // Unboxing of n to int => intValue()
    // At the end Boxing the result to Integer 
    // according to the Return type => Integer.valueOf()
    return n + 7
}