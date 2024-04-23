package pt.isel

import kotlin.system.measureTimeMillis

fun mapSpotifyToArtistBaseline(src: ArtistSpotify) : Artist {
    return Artist(
        src.name,
        State(src.country.name, src.country.idiom),
        src.songs.map { Track(it.name, it.year) }
    )
}

fun main() {
    println("######## Baseline Mapper")
    repeat(10) {
        measureThroughput{
            mapSpotifyToArtistBaseline(src)
        }
    }
    println("######## NaiveMapper Reflect")
    repeat(10) {
        measureThroughput{
            mapperReflect.mapFrom(src)
        }
    }

}

val mapperReflect = NaiveMapper
    .mapper(ArtistSpotify::class, Artist::class)
val src = ArtistSpotify("Muse", Country("UK", "english"), "6284761", listOf(
    Song("Starlight", 2006),
    Song("Hysteria", 2003),
))

const val count = 1000000

fun measureThroughput(block: () -> Unit) {
    val durInMillis = measureTimeMillis {
        repeat(count) {
            block()
        }
    }
    val throughput  = (count.toDouble() / durInMillis).toInt()
    println("Throughput = $throughput ops/ms")
    // println("Duration = ${durInMillis.toDouble()/ count} ms")
}