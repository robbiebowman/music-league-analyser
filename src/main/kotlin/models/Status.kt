package models

enum class Status(val value: String) {
    AcceptingVotes("ACCEPTING_VOTES"),
    Complete("COMPLETE");

    companion object {
        fun fromValue(value: String): Status = when (value) {
            "ACCEPTING_VOTES" -> AcceptingVotes
            "COMPLETE"        -> Complete
            else              -> throw IllegalArgumentException()
        }
    }
}