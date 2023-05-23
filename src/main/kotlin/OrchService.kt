import models.Sentiment

object OrchService {

    fun populateDatabase() {
        val rounds = MusicLeagueService.getRounds(
            Env.musicLeagueToken,
            Env.musicLeagueId
        )
        val roundResults =
            rounds.associateWith { MusicLeagueService.getResults(Env.musicLeagueToken, Env.musicLeagueId, it.id) }
        val members = MusicLeagueService.getMembers(Env.musicLeagueToken, Env.musicLeagueId).map { it.user }

        SqlService.insertEverything(members, roundResults)
    }

    fun createUserPlaylists() {
        val users = SqlService.getUsers()
        users.forEach { SpotifyService.createUserPlaylist("${it.name}'s faves", it.id) }
    }

    fun createGptPlaylists() {
        val users = SqlService.getUsers()
        var u = 1
        users.forEach { user ->
            val songs = SqlService.getSongsUserCommentedOn(user.id)
            val commentsSize = songs.size
            println("${user.name} (${u++}/${users.size}) has left $commentsSize comments. Beginning classification.")
            var i = 1
            val songsWithSentiment = songs.map { song -> Pair(song, GptService.classifyComment(song.comment)).also { println("${i++}/$commentsSize") } }
            val positiveSongs = songsWithSentiment.filter { s -> s.second == Sentiment.VERY_POSITIVE }
            println("Classified. Creating playlist for ${user.name} with ${positiveSongs.size} loved songs.")
            SpotifyService.createPlaylist("${user.name}'s most loved", positiveSongs.map { s -> s.first.spotifyUri })
        }
    }

}