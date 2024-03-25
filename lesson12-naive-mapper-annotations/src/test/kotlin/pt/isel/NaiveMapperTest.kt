package pt.isel

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class NaiveMapperTest {
    @Test
    fun mapArtistSpotifyToArtist() {
        val source = ArtistSpotify("Muse", "UK", "6284761")
        val artist = source.mapTo(Artist::class)
        assertEquals("Muse", artist.name)
        assertEquals("UK", artist.from) // Is still empty
        assertEquals(0, artist.id)
    }
    @Test
    fun mapArtistSpotifyToArtistVersion3() {
        val mapper = NaiveMapper(ArtistSpotify::class, Artist::class)
        val source = ArtistSpotify("Muse", "UK", "6284761")
        val artist = mapper.mapFrom(source)
        assertEquals("Muse", artist.name)
        assertEquals("UK", artist.from) // Is still empty
        assertEquals(0, artist.id)
    }
}