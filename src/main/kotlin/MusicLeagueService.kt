import com.google.gson.Gson
import models.Member
import models.Round
import models.Standing
import models.Standings
import okhttp3.OkHttpClient
import okhttp3.Request

object MusicLeagueService {
    private const val baseUrl = "https://app.musicleague.com/api/v1"
    private val httpClient = OkHttpClient()

    private inline fun <reified T> fetchObject(token: String, endpoint: String): T? {
        val request: Request = Request.Builder()
            .url(baseUrl + endpoint)
            .header("Cookie", token)
            .build()

        val response = httpClient.newCall(request).execute()
        val json = response.body!!.string()
        return try {
            Gson().fromJson(json, T::class.java)
        } catch (e: com.google.gson.JsonSyntaxException) {
            null
        }
    }

    fun getRounds(token: String, leagueId: String): List<Round> {
        return fetchObject<Array<Round>>(token, "/leagues/$leagueId/rounds")?.toList() ?: emptyList()
    }

    fun getResults(token: String, leagueId: String, roundId: String): List<Standing> {
        return fetchObject<Standings>(token, "/leagues/$leagueId/rounds/$roundId/results")?.standings ?: emptyList()
    }

    fun getMembers(token: String, leagueId: String): List<Member> {
        return fetchObject<Array<Member>>(token, "/leagues/$leagueId/members")?.toList() ?: emptyList()
    }
}
