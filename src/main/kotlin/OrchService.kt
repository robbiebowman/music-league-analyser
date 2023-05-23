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

}