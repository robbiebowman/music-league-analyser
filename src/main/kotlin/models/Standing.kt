package models

data class Standing (
    val pointsActual: Long,
    val pointsPossible: Long,
    val rank: Long,
    val submission: Submission,
    val submitterVoted: Boolean,
    val tieBreaker: String,
    val votes: List<Vote>
)

data class Standings(
    val standings: List<Standing>
)