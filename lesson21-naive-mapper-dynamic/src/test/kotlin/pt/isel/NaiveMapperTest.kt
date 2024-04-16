package pt.isel

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NaiveMapperTest {
    @Test
    fun mapArtistSpotifyToArtistVersion3() {
        val mapper = NaiveMapper.mapper(ArtistSpotify::class, Artist::class)
        val source = ArtistSpotify("Muse", Country("UK", "english"), "6284761", listOf(
            Song("Starlight", 2006),
            Song("Hysteria", 2003),
        ))
        val artist = mapper.mapFrom(source)
        assertEquals("Muse", artist.name)
        assertEquals("UK", artist.from.name)
        assertEquals("english", artist.from.idiom)
        assertEquals(0, artist.id)
        val tracks = artist.tracks.iterator()
        assertTrue(tracks.hasNext())
        source.songs.forEach { expected ->
            val actual = tracks.next()
            assertEquals(expected.name, actual.name)
            assertEquals(expected.year, actual.year)
        }
        assertFalse(tracks.hasNext())
    }
}