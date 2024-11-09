package lab3

data class Playlist(
    val name: String,
    val tracks: MutableList<Track> = mutableListOf()
)
