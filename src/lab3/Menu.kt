package lab3

fun printTracksAsTable(tracks: List<Track>) {
    println("=".repeat(100))
    println(
        String.format(
            "%-30s %-15s %-15s %-15s %-15s %-15s",
            "Название трека", "Длительность", "Рейтинг", "Альбом", "Исполнитель", "Жанр"
        )
    )
    println("=".repeat(100))

    tracks.forEach { track ->
        val album = albums.values.find { it.tracks.contains(track) }
        val actor = actors.values.find { it.albums.contains(album) }
        val genre = actor?.genre?.name ?: "Неизвестно"
        println(
            String.format(
                "%-30s %-15d %-15d %-15s %-15s %-15s",
                track.title,
                track.duration,
                track.rating,
                album?.title ?: "Неизвестно",
                actor?.name ?: "Неизвестно",
                genre
            )
        )
    }
    println("=".repeat(100))
}

fun output() {
    while (true) {
        println("\nВсе доступные треки:")

        println(
            """
            Меню:
            1. Создать плейлист
            2. Добавить трек в плейлист
            3. Удалить трек из плейлиста
            4. Вывести треки из плейлиста
            5. Вывести рекомендованные треки
            6. Вывести все треки
            7. Удалить плейлист
            0. Выход
        """.trimIndent()
        )

        when (readlnOrNull()?.toIntOrNull()) {
            1 -> createNewPlaylist()
            2 -> managePlaylistTracks(action = "add")
            3 -> managePlaylistTracks(action = "remove")
            4 -> showPlaylistTracks()
            5 -> showRecommendedTracks()
            6 -> printTracksAsTable(tracks.values.toList())
            7 -> deletePlaylist()
            0 -> {
                println("Выход.")
                return
            }
            else -> println("Неверный пункт меню. Попробуйте снова.")
        }
    }
}

fun createNewPlaylist() {
    print("Введите название плейлиста: ")
    val name = readLine() ?: "Без названия"
    val (id, playlist) = createPlaylist(name)
    playlists.add(playlist)
    println("Плейлист \"$name\" создан с ID: $id")
}

fun managePlaylistTracks(action: String) {
    val playlist = selectPlaylist() ?: return

    when (action) {
        "add" -> {
            print("Введите ID трека для добавления: ")
            val trackId = readLine()?.toIntOrNull()
            if (trackId != null && tracks.containsKey(trackId)) {
                addTrackToPlaylist(playlist, tracks[trackId]!!)
                println("Трек добавлен в плейлист.")
            } else {
                println("Неверный ID трека.")
            }
        }
        "remove" -> {
            printTracksAsTable(playlist.tracks)
            print("Введите ID трека для удаления: ")
            val trackId = readLine()?.toIntOrNull()
            val track = playlist.tracks.find { it.title == "track $trackId" }
            if (track != null) {
                removeTrackFromPlaylist(playlist, track)
                println("Трек удалён из плейлиста.")
            } else {
                println("Трек не найден.")
            }
        }
    }
}

fun showPlaylistTracks() {
    val playlist = selectPlaylist() ?: return
    println("Треки в плейлисте \"${playlist.name}\":")
    printTracksAsTable(playlist.tracks)
}

fun showRecommendedTracks() {
    val recommendedTracks = recommend(playlists, actors.values.toList())
    if (recommendedTracks.isNotEmpty()) {
        println("Рекомендованные треки:")
        printTracksAsTable(recommendedTracks)
    } else {
        println("Нет рекомендаций.")
    }
}

fun deletePlaylist() {
    val playlist = selectPlaylist() ?: return
    playlists.remove(playlist)
    println("Плейлист \"${playlist.name}\" удалён.")
}

fun selectPlaylist(): Playlist? {
    if (playlists.isEmpty()) {
        println("Нет доступных плейлистов.")
        return null
    }

    println("Доступные плейлисты:")
    playlists.forEachIndexed { index, playlist -> println("${index + 1}. ${playlist.name}") }
    print("Выберите плейлист: ")
    val playlistIndex = readLine()?.toIntOrNull()?.minus(1)
    return if (playlistIndex != null && playlistIndex in playlists.indices) {
        playlists[playlistIndex]
    } else {
        println("Неверный номер плейлиста.")
        null
    }
}