package models

data class MemberStats(
    val user: User,
    val biggestHater: User?,
    val haterVoteTotal: Int?,
    val biggestFan: User?,
    val fanVoteTotal: Int?
)