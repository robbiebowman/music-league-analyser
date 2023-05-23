import com.theokanning.openai.completion.CompletionRequest
import com.theokanning.openai.completion.chat.ChatCompletionRequest
import com.theokanning.openai.completion.chat.ChatMessage
import com.theokanning.openai.service.OpenAiService
import models.Sentiment
import java.time.Duration

object GptService {
    private val openAiService: OpenAiService
        get() = OpenAiService(Env.openaiApiKey, Duration.ofSeconds(30))

    fun classifyComment(comment: String): Sentiment {
        val completionRequest = ChatCompletionRequest.builder().model("gpt-3.5-turbo").messages(
            listOf(
                ChatMessage(
                    "system",
                    "You are a classifier bot. The user will post a comment about a song they've heard. Your job is to reply with one of the following labels: NEGATIVE, NEUTRAL, POSITIVE, VERY_POSITIVE based on how much the user seems to have liked this song."
                ),
                ChatMessage(
                    "user", comment
                ),
            )
        ).user("musicleagueanalyser").n(1).build()
        val result = executeWithRetry(retries = 3) {
            openAiService.createChatCompletion(completionRequest)
        }
        val content = result.choices.first().message.content
        return responseToSentiment(content)
    }

    private fun responseToSentiment(response: String): Sentiment {
        return if (response.contains(Sentiment.VERY_POSITIVE.name, ignoreCase = true)) Sentiment.VERY_POSITIVE
        else if (response.contains(Sentiment.POSITIVE.name, ignoreCase = true)) Sentiment.POSITIVE
        else if (response.contains(Sentiment.NEUTRAL.name, ignoreCase = true)) Sentiment.NEUTRAL
        else Sentiment.NEGATIVE
    }

    private inline fun <T> executeWithRetry(retries: Int, call: () -> T): T {
        for (i in 0 until retries) {
            return try {
                call()
            } catch (e: Exception) {
                println("Failed. Retrying...")
                continue
            }
        }
        return call()
    }
}