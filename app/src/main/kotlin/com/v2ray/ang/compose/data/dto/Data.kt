package com.v2ray.ang.compose.data.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Data(
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("type")
    var type: String? = null,
    @SerializedName("message")
    var message: String? = null
)