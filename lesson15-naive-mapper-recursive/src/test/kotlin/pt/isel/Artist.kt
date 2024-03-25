package pt.isel

/**
 * 2nd version with immutable properties
 */
class Artist(
    val id: Int = 0,
    val name: String,
    val from: String
)
/**
 * 1st version with mutable properties
 */
/*
class Artist() {
    var id: Int = 0
    var name: String = ""
    var from: String = ""
}*/