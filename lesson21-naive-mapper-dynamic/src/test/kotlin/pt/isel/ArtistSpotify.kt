package pt.isel

class ArtistSpotify(
    val name: String,
    @MapProp("from") val country: Country = Country("", ""),
    val id: String,
    @MapProp("tracks") val songs: List<Song>
)