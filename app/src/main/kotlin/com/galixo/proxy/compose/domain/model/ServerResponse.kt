package com.galixo.proxy.compose.domain.model

import androidx.annotation.Keep
import com.galixo.proxy.compose.data.dto.ServersDto

@Keep
data class ServerResponse(
    val code: Int,
    val servers: List<ServersDto>,
    val status: String
)