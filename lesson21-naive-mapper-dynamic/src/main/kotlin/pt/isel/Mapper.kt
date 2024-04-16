package pt.isel

interface Mapper<T> {
    fun mapFrom(source: Any): T
}