import kotlinx.coroutines.*

fun main(args: Array<String>) {
    runBlocking {
        SpotifyService.createUserPlaylist("test")
        val rounds = MusicLeagueService.getRounds(
            Env.musicLeagueToken,
            Env.musicLeagueId
        )
        val roundResults = rounds.associateWith { MusicLeagueService.getResults(Env.musicLeagueToken, Env.musicLeagueId, it.id) }
        val members = MusicLeagueService.getMembers(Env.musicLeagueToken, Env.musicLeagueId).map { it.user }

        //SqlService.insertEverything(members, roundResults)
    }
}