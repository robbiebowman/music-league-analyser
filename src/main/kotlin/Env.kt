object Env {
    private val env = System.getenv()

    val musicLeagueToken = env.get("music_league_token")!!
    val musicLeagueId = env.get("league_id")!!
    val spotifyClientId = env.get("spotify_client_id")!!
    val spotifyClientSecret = env.get("spotify_client_secret")!!
    val spotifyAccessToken = env.get("spotify_access_token")!!
    val spotifyRefreshToken = env.get("spotify_refresh_token")!!
}