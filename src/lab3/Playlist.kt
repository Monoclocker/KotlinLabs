package lab3

var currentPlaylistId = 1

data class Playlist(
    val name: String,
    val tracks: MutableList<Track> = mutableListOf()
)

fun createPlaylist(name: String): Pair<Int, Playlist>{
    return Pair(currentPlaylistId++, Playlist(name))
}

fun addTrackToPlaylist(playlist: Playlist, track: Track) {
    playlist.tracks.add(track)
}

fun removeTrackFromPlaylist(playlist: Playlist, track: Track) {
    playlist.tracks.remove(track)
}

fun removePlaylists(playlist: Playlist){
    playlists.remove(playlist)
}