import com.neovisionaries.i18n.CountryCode
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.model_objects.specification.Artist
import se.michaelthelin.spotify.model_objects.specification.Track
import java.net.URI


object SpotifyService {

    private val spotifyApi = buildUserAgentSpotifyApi()

    private fun buildReadOnlySpotifyApi(): SpotifyApi {
        val api = SpotifyApi.Builder()
            .setClientId(Env.spotifyClientId)
            .setClientSecret(Env.spotifyClientSecret)
            .build()
        val accessToken = api.clientCredentials().build().execute().accessToken
        api.accessToken = accessToken
        return api
    }

    private fun buildUserAgentSpotifyApi(): SpotifyApi {
        val api = SpotifyApi.Builder()
            .setClientId(Env.spotifyClientId)
            .setClientSecret(Env.spotifyClientSecret)
            .setRedirectUri(URI("http://localhost/spotify_redirect"))
            .build()
        val uri = api.authorizationCodeUri().scope("playlist-modify-public").build().execute()
        println(uri)
        println("Paste the code from the redirect URL after you authorise:")
        val code = readLine()!!
        val accessToken = api.authorizationCode(code).build().execute().accessToken
        api.accessToken = accessToken
        println("Authorisation success! Beginning job...")
        return api
    }

    fun getSongDetails(songUri: String): Pair<Track, Artist> {
        val songId = songUri.split(':').last()

        val track = spotifyApi.getTrack(songId)
            .market(CountryCode.US)
            .build().execute()

        val artist = spotifyApi.getArtist(track.artists.first().id)
            .build().execute()

        return Pair(track, artist)
    }

    fun createUserPlaylist(playlistName: String, musicLeagueUserId: String) {
        val songIds = SqlService.getSongsUserVotedFor(musicLeagueUserId).shuffled()
        createPlaylist(playlistName, songIds)
    }

    fun createPlaylist(playlistName: String, songUris: List<String>) {
        if (songUris.isNotEmpty()) {
            val userId = spotifyApi.currentUsersProfile.build().execute().id
            val playlist = spotifyApi.createPlaylist(userId, playlistName).build().execute()
            spotifyApi.addItemsToPlaylist(playlist.id, songUris.toTypedArray()).build().execute()
        }
    }
}
