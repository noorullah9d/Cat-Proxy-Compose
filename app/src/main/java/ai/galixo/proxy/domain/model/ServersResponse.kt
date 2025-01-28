package ai.galixo.proxy.domain.model

import androidx.annotation.Keep
import ai.galixo.proxy.data.dto.ServerGroup
import com.google.gson.annotations.SerializedName

@Keep
data class ServersResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("status") val status: String,
    @SerializedName("servers") val servers: List<ServerGroup>
)