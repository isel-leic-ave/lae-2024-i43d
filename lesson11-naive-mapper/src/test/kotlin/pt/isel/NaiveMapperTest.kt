package pt.isel

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class NaiveMapperTest {
    @Test
    fun mapArtistSpotifyToArtist() {
        val from = ArtistSpotify("Muse", "UK", "6284761")
        val artist = from.mapTo(Artist::class)
        assertEquals("Muse", artist.name)
        assertEquals("", artist.from) // Is still empty
        assertEquals(0, artist.id)
    }
}