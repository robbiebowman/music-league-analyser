package models

data class Round (
    val id: String,
    val name: String,
    val completed: String,
    val description: String,
    val downvotesPerUser: Long,
    val highStakes: Boolean,
    val leagueId: String,
    val maxDownvotesPerSong: Long,
    val maxUpvotesPerSong: Long,
    val playlistUrl: String,
    val sequence: Long,
    val songsPerUser: Long,
    val startDate: String,
    val status: String,
    val submissionsDue: String,
    val upvotesPerUser: Long,
    val votesDue: String,
    val templateId: String
)