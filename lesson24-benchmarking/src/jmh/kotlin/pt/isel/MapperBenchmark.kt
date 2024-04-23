package pt.isel

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import pt.isel.*

@State(Scope.Benchmark)
open class MapperBenchmark {

    private val mapperReflect = NaiveMapper
        .mapper(ArtistSpotify::class, Artist::class)
    private val src = ArtistSpotify("Muse", Country("UK", "english"), "6284761", listOf(
        Song("Starlight", 2006),
        Song("Hysteria", 2003),
    ))

    @Benchmark
    fun baselineMapper(): Artist {
        return mapSpotifyToArtistBaseline(src)
    }

    @Benchmark
    fun reflectMapper(): Artist {
        return mapperReflect.mapFrom(src)
    }
}