import models.Round
import models.Standing
import models.User
import org.ktorm.database.Database
import org.ktorm.database.SqlDialect
import org.ktorm.dsl.*
import org.ktorm.schema.*
import org.ktorm.support.mysql.MySqlDialect
import org.ktorm.support.mysql.insertOrUpdate

object SqlService {

    private val database = Database.connect(
        url = "jdbc:mysql://localhost:3306/db",
        driver = "com.mysql.jdbc.Driver",
        user = "root",
        password = "root",
        dialect = MySqlDialect()
    )

    fun insertEverything(members: List<models.User>, roundResults: Map<Round, List<Standing>>) {
        members.forEach { m ->
            database.insertOrUpdate(Users) {
                set(it.id, m.id)
                set(it.name, m.name)
                onDuplicateKey{
                    set(it.name, m.name)
                }
            }
        }
        roundResults.keys.forEach { r ->
            database.insertOrUpdate(Rounds) {
                set(it.id, r.id)
                set(it.name, r.name)
                set(it.description, r.description)
                set(it.playlist_uri, r.playlistUrl)
                onDuplicateKey{
                    set(it.name, r.name)
                    set(it.description, r.description)
                }
            }
        }
        roundResults.forEach { rr ->
            rr.value.forEach { s ->
                val spotifyDetails = SpotifyService.getSongDetails(s.submission.spotifyUri)
                val submissionId = s.submission.submitterId + s.submission.created
                database.insertOrUpdate(Submissions) {
                    set(it.id, submissionId)
                    set(it.user_id, s.submission.submitterId)
                    set(it.spotify_uri, s.submission.spotifyUri)
                    set(it.round_id, rr.key.id)
                    set(it.comment, s.submission.comment)
                    onDuplicateKey{
                        set(it.comment, s.submission.comment)
                    }
                }
                val song = spotifyDetails.first
                val artist = spotifyDetails.second
                database.insertOrUpdate(Songs) {
                    set(it.spotify_uri, song.uri)
                    set(it.name, song.name)
                    set(it.artist_id, artist.id)
                    set(it.duration_ms, song.durationMs)
                    set(it.popularity_num, song.popularity)
                    onDuplicateKey{
                        set(it.popularity_num, song.popularity)
                    }
                }
                database.insertOrUpdate(Artists) {
                    set(it.id, artist.id)
                    set(it.spotify_uri, artist.uri)
                    set(it.name, artist.name)
                    set(it.popularity_num, artist.popularity)
                    onDuplicateKey{
                        set(it.popularity_num, artist.popularity)
                    }
                }
                artist.genres.forEach { g ->
                    database.insertOrUpdate(ArtistsGenres) {
                        set(it.artist_id, artist.id)
                        set(it.name, g)
                        onDuplicateKey{
                            set(it.name, g)
                        }
                    }
                }
                s.votes.forEach { v ->
                    database.insertOrUpdate(Votes) {
                        set(it.user_id, v.voterId)
                        set(it.submission_id, submissionId)
                        set(it.weight, v.weight)
                        set(it.comment, v.comment)
                        onDuplicateKey{
                            set(it.comment, v.comment)
                        }
                    }
                }
            }
        }
    }

    fun getSongsUserVotedFor(musicLeagueUserId: String): List<String> {
        val query = database.from(Submissions)
            .innerJoin(Votes, on = Votes.submission_id eq Submissions.id)
            .select()
            .where((Votes.user_id eq musicLeagueUserId) and (Votes.weight greater 0))

        return query.map { row -> row[Submissions.spotify_uri] }.filterNotNull()
    }

    fun getSongsUserCommentedOn(musicLeagueUserId: String): List<SongComment> {
        val query = database.from(Submissions)
            .innerJoin(Votes, on = Votes.submission_id eq Submissions.id)
            .select()
            .where((Votes.user_id eq musicLeagueUserId) and (Votes.comment.isNotNull()) and (Votes.comment notEq ""))

        return query.map { row -> SongComment(row[Submissions.spotify_uri]!!, row[Votes.comment]!!) }
    }

    fun getUsers(): List<SqlService.User> {
        return database.from(Users)
            .select().map { row -> User(row[Users.id]!!, row[Users.name]!!) }
    }

    data class User(val id: String, val name: String)
    data class SongComment(val spotifyUri: String, val comment: String)

    object Users : Table<Nothing>("users") {
        val id = varchar("id").primaryKey()
        val name = varchar("name")
    }

    object Votes : Table<Nothing>("votes") {
        val user_id = varchar("user_id").primaryKey()
        val submission_id = varchar("submission_id").primaryKey()
        val weight = long("weight")
        val comment = text("comment")
    }

    object Submissions : Table<Nothing>("submissions") {
        val id = varchar("id").primaryKey()
        val user_id = varchar("user_id")
        val spotify_uri = varchar("spotify_uri")
        val round_id = varchar("round_id")
        val comment = text("comment")
    }

    object Rounds : Table<Nothing>("rounds") {
        val id = varchar("id").primaryKey()
        val name = varchar("name")
        val description = varchar("description")
        val playlist_uri = varchar("playlist_uri")
    }

    object Songs : Table<Nothing>("songs") {
        val spotify_uri = varchar("spotify_uri").primaryKey()
        val name = varchar("name")
        val artist_id = varchar("artist_id")
        val duration_ms = int("duration_ms")
        val popularity_num = int("popularity_num")
    }

    object Artists : Table<Nothing>("artists") {
        val id = varchar("id").primaryKey()
        val spotify_uri = varchar("spotify_uri")
        val name = varchar("name")
        val popularity_num = int("popularity_num")
    }

    object ArtistsGenres : Table<Nothing>("artists_genres") {
        val artist_id = varchar("artist_id").primaryKey()
        val name = varchar("name").primaryKey()
    }
}