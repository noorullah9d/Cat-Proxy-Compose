package com.v2ray.ang.compose.data

import androidx.annotation.Keep

@Keep
data class LanguageModel(
    val languageName: String,
    val languageLocalName: String,
    val languageCode: String,
    val flagDrawable: Int,
    val isSelected: Boolean = false,
)
