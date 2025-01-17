package ai.galixo.proxy.data.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class SupportMessageDto(
    @SerializedName("data")
    var data: Data? = Data()
)
