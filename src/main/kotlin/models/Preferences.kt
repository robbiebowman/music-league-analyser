package models

data class Preferences (
    val id: String,
    val trackCount: Long,
    val highStakes: Boolean,
    val maxMembers: Long,
    val upvoteBankSize: Long,
    val maxUpvotesPerSong: Long,
    val downvoteBankSize: Long,
    val maxDownvotesPerSong: Long,
    val submissionCommentVisibility: String,
    val submissionIntervalDays: Long,
    val voteIntervalDays: Long,
    val pacing: String
)