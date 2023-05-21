import com.neovisionaries.i18n.CountryCode
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.model_objects.specification.Artist
import se.michaelthelin.spotify.model_objects.specification.Track
import se.michaelthelin.spotify.requests.data.tracks.GetTrackRequest
import java.net.URI


object SpotifyService {

    private val spotifyApi = buildSpotifyApi()

    private fun buildSpotifyApi(): SpotifyApi {
        val api = SpotifyApi.Builder()
            .setClientId(Env.spotifyClientId)
            .setClientSecret(Env.spotifyClientSecret)
            .setAccessToken(Env.spotifyAccessToken)
            .setRefreshToken(Env.spotifyRefreshToken)
            .build()
        val accessToken = api.authorizationCodeRefresh().build().execute().accessToken
        api.accessToken = accessToken
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

    fun createUserPlaylist(userId: String) {
        spotifyApi.createPlaylist("123", "testabc").build().execute()
    }
}
