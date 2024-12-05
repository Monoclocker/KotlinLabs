package lab3

import kotlin.random.Random


val actors: MutableMap<Int,Actor> = mutableMapOf()
val albums: MutableMap<Int, Album> = mutableMapOf()
val tracks: MutableMap<Int, Track> = mutableMapOf()
val playlists: MutableList<Playlist> = mutableListOf()

fun generateMockData(){
    val actorsCount: Int = Random.nextInt(5, 10)

    var albumCurrentId = 1
    var trackCurrentId = 1

    for (i in 1..actorsCount){

        val albumsCount = Random.nextInt(3, 7)
        val albumList: MutableList<Album> = mutableListOf()

        for (j in 1..albumsCount){

            val trackList: MutableList<Track> = mutableListOf()
            val trackCount = Random.nextInt(6, 12)

            for (k in 1..trackCount){

                val newTrack = Track("track $trackCurrentId", Random.nextInt(60, 180), Random.nextInt(1, 6))
                tracks[trackCurrentId] = newTrack
                trackList.add(newTrack)
                trackCurrentId++

            }

            val newAlbum = Album("album $albumCurrentId", Random.nextInt(2000, 2025), trackList)
            albums[albumCurrentId] = newAlbum
            albumCurrentId++
            albumList.add(newAlbum)
        }

        val newActor = Actor("actor $i", Genre.entries.shuffled().first(), albumList)

        actors[i] = newActor
    }
}

fun searchTracksByArtist(artistName: String,
    operation: (artistName: String) -> List<Track>): List<Track>{
    return operation(artistName)
}

fun searchTracksByGenre(genre: String,
                        operation: (genre: String) -> List<Track>): List<Track> {

    return operation(genre)
}

fun searchTracksByRating(minRating: Int,
                         operation: (rating: Int) -> List<Track>): List<Track>{
    return operation(minRating)
}

fun sortTracks(playlist: Playlist, operation: (Playlist) -> Unit): Playlist {
    val playlistForSort = playlist.copy()
    operation(playlistForSort)
    return playlistForSort
}




fun main() {
    generateMockData()
    output()
}

