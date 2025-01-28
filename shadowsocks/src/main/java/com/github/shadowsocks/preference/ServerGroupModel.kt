package com.github.shadowsocks.preference

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ServerGroupModel(
    var countryName: String = "",
    var flag: String = "",
    var data: ArrayList<ServerModel> = arrayListOf()
) : Parcelable