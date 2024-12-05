package lab3

import kotlin.math.abs

data class Track(
    val title: String,
    val duration: Int,
    val rating: Int
)

fun recommend(playlists: List<Playlist>, actors: List<Actor>): List<Track> {
    if (playlists.isEmpty() || playlists.all { it.tracks.isEmpty() }) {
        return emptyList()
    }

    if (actors.isEmpty()) {
        return emptyList()
    }

    val albums = actors.flatMap { it.albums }

    val playlistUsedTrack = playlists.flatMap { it.tracks }.distinct()

    val playlistUsedAlbums = albums.filter { album ->
        playlistUsedTrack.any { track -> album.tracks.contains(track) }
    }.distinct()

    val playlistUsedActor = actors.filter { actor ->
        playlistUsedAlbums.any { album -> actor.albums.contains(album) }
    }.distinct()

    val meanRating: Double = if (playlistUsedTrack.isNotEmpty()) {
        playlistUsedTrack.sumOf { it.rating }.toDouble() / playlistUsedTrack.size
    } else 0.0

    val avgYear: Double = if (playlistUsedAlbums.isNotEmpty()) {
        playlistUsedAlbums.sumOf { it.releaseYear }.toDouble() / playlistUsedAlbums.size
    } else 0.0

    val avgDuration: Double = if (playlistUsedTrack.isNotEmpty()) {
        playlistUsedTrack.sumOf { it.duration }.toDouble() / playlistUsedTrack.size
    } else 0.0

    val popularGenre = playlistUsedActor
        .groupingBy { it.genre }
        .eachCount()
        .maxByOrNull { it.value }
        ?.key ?: return emptyList()

    val filteredActors = actors.filter { it.genre == popularGenre }

    val filteredAlbums = filteredActors
        .flatMap { it.albums }
        .filter { abs(it.releaseYear - avgYear) <= 5 }

    return filteredAlbums
        .flatMap { it.tracks }
        .filter { abs(it.rating - meanRating) < 1 && abs(it.duration - avgDuration) < 20 }
}
