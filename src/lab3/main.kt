package lab3

fun createPlaylist(name: String): Playlist = Playlist(name)

fun addTrackToPlaylist(playlist: Playlist, track: Track) {
    playlist.tracks.add(track)
}

fun removeTrackFromPlaylist(playlist: Playlist, track: Track) {
    playlist.tracks.remove(track)
}

fun recommendTracks(genre: String, minRating: Int,
    operation: (genre: String, minRating: Int) -> List<Track>): List<Track> {
    return operation(genre, minRating)
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

// Анализ предпочтений пользователя для рекомендаций (на основе среднего рейтинга по жанрам)
fun analyzeUserPreferences(library: List<Artist>): Map<String, Double> {
    val genreRatings = mutableMapOf<String, MutableList<Int>>()
    library.forEach { artist ->
        artist.albums.forEach { album ->
            album.tracks.forEach { track ->
                genreRatings.computeIfAbsent(artist.genre) { mutableListOf() }.add(track.rating)
            }
        }
    }
    return genreRatings.mapValues { (_, ratings) -> ratings.average() }
}

fun main() {
    val track1 = Track("Song One", 180, 5)
    val track2 = Track("Song Two", 200, 4)
    val track3 = Track("Song Three", 240, 3)

    val album = Album("Album One", 2021, listOf(track1, track2, track3))

    val artist = Artist("Artist One", "Rock", listOf(album))
    val library = listOf(artist)

    val playlist = createPlaylist("My Playlist")
    addTrackToPlaylist(playlist, track1)
    addTrackToPlaylist(playlist, track2)

    val playlistSortedByRating = sortTracks(playlist){ playlistForSort ->
        playlistForSort.tracks.sortByDescending { it.rating }
    }
    println("Сортировка по рейтингу: $playlistSortedByRating")

    val playlistSortedByDuration = sortTracks(playlist){ playlistForSort ->
        playlistForSort.tracks.sortBy { it.duration }
    }
    println("Сортировка по длительности: $playlistSortedByDuration")

    val playlistSortedByTitle = sortTracks(playlist){ playlistForSort ->
        playlistForSort.tracks.sortBy { it.title }
    }
    println("Сортировка по названию: $playlistSortedByTitle")

    val searchByRating = searchTracksByRating(3){ minRating ->
        library.flatMap { artist ->
            artist.albums.flatMap { album ->
                album.tracks.filter { it.rating >= minRating }
            }
        }
    }
    println("Поиск по оценке: $searchByRating")

    val searchByGenre = searchTracksByGenre("Rock") { genre ->
        library.filter { it.genre == genre }
            .flatMap { it.albums.flatMap { album -> album.tracks } }
    }
    println("Поиск по жанру: $searchByGenre")

    val searchByArtist = searchTracksByArtist("Artist One"){
        artistName -> library.filter {
            it.name == artistName
        }.flatMap {
            it.albums.flatMap { album -> album.tracks }
        }
    }
    println("Поиск по артисту: $searchByArtist")

    val recommendations = recommendTracks("Rock", 4) {genre, rating ->
        library.flatMap { artist ->
            artist.albums.flatMap { album ->
                album.tracks.filter { it.rating >= rating && artist.genre == genre }
            }
        }
    }
    println("Рекомендации: $recommendations")

    val userPreferences = analyzeUserPreferences(library)
    println("Предпочтения пользователя: $userPreferences")
}

