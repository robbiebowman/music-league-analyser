import kotlinx.coroutines.*

fun main(args: Array<String>) {
    runBlocking {
        // Your token here
        val token = "session=......."

        // Your league id here
        val leagueId = "b589........"
        
        val rounds = MusicLeagueService.getRounds(
            token,
            leagueId
        )
        val roundResults = rounds.associateWith { MusicLeagueService.getResults(token, leagueId, it.id) }
        val members = MusicLeagueService.getMembers(token, leagueId).map { it.user }

        SqlService.insertEverything(members, roundResults)
    }
}