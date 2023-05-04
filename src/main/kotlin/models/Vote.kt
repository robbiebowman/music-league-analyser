package models

data class Vote (
    val comment: String,
    val created: String,
    val spotifyUri: String,
    val voterId: String,
    val weight: Long
)