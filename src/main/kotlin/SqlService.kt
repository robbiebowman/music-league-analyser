import models.Round
import models.Standing
import models.User
import org.ktorm.database.Database
import org.ktorm.dsl.insert
import org.ktorm.schema.*

object SqlService {
    fun insertEverything(members: List<User>, roundResults: Map<Round, List<Standing>>) {
        val database = Database.connect(
            "jdbc:mysql://localhost:3306/db",
            driver = "com.mysql.jdbc.Driver", user = "root", password = "root"
        )

        members.forEach { m ->
            database.insert(Users) {
                set(it.id, m.id)
                set(it.name, m.name)
            }
        }
        roundResults.keys.forEach { r ->
            database.insert(Rounds) {
                set(it.id, r.id)
                set(it.name, r.name)
                set(it.description, r.description)
                set(it.playlist_uri, r.playlistUrl)
            }
        }
        roundResults.forEach { rr ->
            rr.value.forEach { s ->
                val spotifyDetails = SpotifyService.getSongDetails(s.submission.spotifyUri)
                val submissionId = s.submission.submitterId + s.submission.created
                database.insert(Submissions) {
                    set(it.id, submissionId)
                    set(it.user_id, s.submission.submitterId)
                    set(it.spotify_uri, s.submission.spotifyUri)
                    set(it.round_id, rr.key.id)
                    set(it.comment, s.submission.comment)
                }
                val song = spotifyDetails.first
                val artist = spotifyDetails.second
                database.insert(Songs) {
                    set(it.spotify_uri, song.uri)
                    set(it.name, song.name)
                    set(it.artist_id, artist.id)
                    set(it.duration_ms, song.durationMs)
                    set(it.popularity_num, song.popularity)
                }
                database.insert(Artists) {
                    set(it.id, artist.id)
                    set(it.spotify_uri, artist.uri)
                    set(it.name, artist.name)
                    set(it.popularity_num, artist.popularity)
                }
                artist.genres.forEach { g ->
                    database.insert(ArtistsGenres) {
                        set(it.artist_id, artist.id)
                        set(it.name, g)
                    }
                }
                s.votes.forEach { v ->
                    database.insert(Votes) {
                        set(it.user_id, v.voterId)
                        set(it.submission_id, submissionId)
                        set(it.weight, v.weight)
                        set(it.comment, v.comment)
                    }
                }
            }
        }
    }

    object Users : Table<Nothing>("users") {
        val id = varchar("id")
        val name = varchar("name")
    }

    object Votes : Table<Nothing>("votes") {
        val user_id = varchar("user_id")
        val submission_id = varchar("submission_id")
        val weight = long("weight")
        val comment = text("comment")
    }

    object Submissions : Table<Nothing>("submissions") {
        val id = varchar("id")
        val user_id = varchar("user_id")
        val spotify_uri = varchar("spotify_uri")
        val round_id = varchar("round_id")
        val comment = text("comment")
    }

    object Rounds : Table<Nothing>("rounds") {
        val id = varchar("id")
        val name = varchar("name")
        val description = varchar("description")
        val playlist_uri = varchar("playlist_uri")
    }

    object Songs : Table<Nothing>("songs") {
        val spotify_uri = varchar("spotify_uri")
        val name = varchar("name")
        val artist_id = varchar("artist_id")
        val duration_ms = int("duration_ms")
        val popularity_num = int("popularity_num")
    }

    object Artists : Table<Nothing>("artists") {
        val id = varchar("id")
        val spotify_uri = varchar("spotify_uri")
        val name = varchar("name")
        val popularity_num = int("popularity_num")
    }

    object ArtistsGenres : Table<Nothing>("artists_genres") {
        val artist_id = varchar("artist_id")
        val name = varchar("name")
    }
}