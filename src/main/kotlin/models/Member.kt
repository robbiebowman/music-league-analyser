package models

data class Member (
    val created: String,
    val isAdmin: Boolean,
    val isPlayer: Boolean,
    val user: User
)