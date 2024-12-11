package com.galixo.proxy.compose.data.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class MessageResponseDto(
    @SerializedName("code")
    var code: Int = -1,
    @SerializedName("status")
    var status: String? = null,
    @SerializedName("URL")
    var URL: String? = null,
    @SerializedName("message")
    var message: String? = null
)