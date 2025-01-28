package ai.galixo.proxy.data.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Server(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("ipaddress") var ipaddress: String? = null,
    @SerializedName("password") var password: String? = null,
    @SerializedName("country_name") var countryName: String? = null,
    @SerializedName("city_name") var cityName: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("flag") var flag: String? = null,
    @SerializedName("protocol") var protocol: String? = null,
    @SerializedName("port") var port: String? = null,
    @SerializedName("encryption") var encryption: String? = null,
    @SerializedName("timezone") var timezone: String? = null,
    @SerializedName("lat") var lat: String? = null,
    @SerializedName("lng") var lng: String? = null,
    @SerializedName("server_type") var serverType: String? = null
)