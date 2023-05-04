package models

data class Submission (
    val created: String,
    val submitterId: String,
    val spotifyUri: String,
    val comment: String,
    val commentVisibility: String
)