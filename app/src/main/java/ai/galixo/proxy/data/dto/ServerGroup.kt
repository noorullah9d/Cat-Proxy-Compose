package ai.galixo.proxy.data.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ServerGroup(
    @SerializedName("cname") var countryName: String? = null,
    @SerializedName("flag") var flag: String? = null,
    @SerializedName("list") var data: ArrayList<Server> = arrayListOf()
)