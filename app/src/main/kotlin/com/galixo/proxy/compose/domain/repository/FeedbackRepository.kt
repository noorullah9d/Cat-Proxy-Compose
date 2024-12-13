package com.galixo.proxy.compose.domain.repository

interface FeedbackRepository {
    suspend fun sendFeedBack(message: String, callback: (Boolean) -> Unit)
}