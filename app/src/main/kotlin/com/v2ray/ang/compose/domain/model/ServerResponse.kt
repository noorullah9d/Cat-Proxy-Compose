package com.v2ray.ang.compose.domain.model

import androidx.annotation.Keep
import com.v2ray.ang.compose.data.dto.ServersDto

@Keep
data class ServerResponse(
    val code: Int,
    val servers: List<ServersDto>,
    val status: String
)