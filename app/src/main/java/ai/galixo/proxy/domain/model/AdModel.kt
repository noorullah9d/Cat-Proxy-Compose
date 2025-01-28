package ai.galixo.proxy.domain.model

import com.google.gson.annotations.SerializedName

class AdModel(
    @SerializedName("show") var show: Boolean = false,
    @SerializedName("priority") var priority: Int = 0,
    @SerializedName("ad_id") var adId: String = ""
)
