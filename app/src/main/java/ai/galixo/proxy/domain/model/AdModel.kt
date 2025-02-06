package ai.galixo.proxy.domain.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class AdModel(
    @SerializedName("show") var show: Boolean = false,
    @SerializedName("priority") var priority: Int = 0,
    @SerializedName("ad_id") var adId: String = ""
)
