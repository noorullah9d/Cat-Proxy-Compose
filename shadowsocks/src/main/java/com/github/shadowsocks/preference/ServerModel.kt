package com.github.shadowsocks.preference

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ServerModel(
    var id: Int = 0,
    var ipaddress: String = "",
    var password: String = "",
    var countryName: String = "",
    var cityName: String = "",
    var type: String = "",
    var flag: String = "",
    var protocol: String = "",
    var port: String = "",
    var encryption: String = "",
    var ping: Long = 100,
    var isPremium: Boolean = false,
    var timezone: String = "",
    var lat: String = "",
    var lng: String = "",
    var serverType: String = ""
) : Parcelable