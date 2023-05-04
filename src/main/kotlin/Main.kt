import com.google.gson.Gson
import kotlinx.coroutines.*
import models.MemberStats

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

        val userStats = members.map { m ->
            val votesByUser =
                roundResults.map { r -> r.value.firstOrNull { result -> result.submission.submitterId == m.id } }
                    .filterNotNull()
                    .flatMap { results -> results.votes }.groupBy { it.voterId }

            val userFanRanking =
                votesByUser.entries.associate { v -> members.firstOrNull { it.id == v.key } to v.value.sumOf { it.weight } }
                    .filter { it.key != null }
            val hater = userFanRanking.minBy { it.value }.key
            val fan = userFanRanking.maxBy { it.value }.key
            MemberStats(
                user = m,
                biggestHater = hater,
                haterVoteTotal = votesByUser[hater?.id]?.sumOf { it.weight }?.toInt(),
                biggestFan = fan,
                fanVoteTotal = votesByUser[fan?.id]?.sumOf { it.weight }?.toInt()
            )
        }
        println(Gson().toJson(userStats))
    }
}