package models

data class League (
    val created: String,
    val description: String,
    val id: String,
    val images: Images,
    val members: List<Member>,
    val name: String,
    val numMembers: Long,
    val numRounds: Long,
    val ownerId: String,
    val preferences: Preferences,
    val spaceId: String,
    val status: String,
    val tier: String,
    val visibility: String
)