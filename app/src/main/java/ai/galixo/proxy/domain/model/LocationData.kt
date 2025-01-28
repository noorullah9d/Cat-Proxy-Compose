package ai.galixo.proxy.domain.model

import androidx.annotation.Keep

@Keep
data class LocationData(
    val locationName: String = "",
    val ipAddress: String = "",
    val timeZone: String = "",
    val flag: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
)