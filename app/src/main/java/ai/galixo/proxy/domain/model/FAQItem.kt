package ai.galixo.proxy.domain.model

import androidx.annotation.Keep

@Keep
data class FAQItem(
    val question: String,
    val answer: String
)