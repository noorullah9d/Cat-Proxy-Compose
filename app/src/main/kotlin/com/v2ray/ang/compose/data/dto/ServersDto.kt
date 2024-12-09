package com.v2ray.ang.compose.data.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ServersDto(
    @SerializedName("cname") var cityName: String? = null,
    @SerializedName("flag") var flag: String? = null,
    @SerializedName("list") var data: ArrayList<ServerDto> = arrayListOf()
)