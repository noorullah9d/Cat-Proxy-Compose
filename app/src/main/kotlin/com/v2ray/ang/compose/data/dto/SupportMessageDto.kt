package com.v2ray.ang.compose.data.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class SupportMessageDto(
    @SerializedName("data")
    var data: Data? = Data()
)
