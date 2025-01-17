package ai.galixo.proxy.domain.model

import androidx.annotation.Keep
import ai.galixo.proxy.data.dto.ServersDto

@Keep
data class ServerResponse(
    val code: Int,
    val servers: List<ServersDto>,
    val status: String
)