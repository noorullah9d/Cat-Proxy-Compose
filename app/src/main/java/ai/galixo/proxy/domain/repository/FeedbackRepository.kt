package ai.galixo.proxy.domain.repository

interface FeedbackRepository {
    suspend fun sendFeedBack(message: String, callback: (Boolean) -> Unit)
}