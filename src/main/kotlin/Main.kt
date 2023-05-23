import kotlinx.coroutines.*

fun main(args: Array<String>) {
    runBlocking {
        OrchService.createUserPlaylists()
    }
}