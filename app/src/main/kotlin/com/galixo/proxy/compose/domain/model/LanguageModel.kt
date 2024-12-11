package com.galixo.proxy.compose.domain.model

import androidx.annotation.Keep

@Keep
data class LanguageModel(
    val languageName: String,
    val languageLocalName: String,
    val languageCode: String,
    val flagDrawable: Int,
    val isSelected: Boolean = false,
)
