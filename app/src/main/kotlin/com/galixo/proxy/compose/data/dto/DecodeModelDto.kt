package com.galixo.proxy.compose.data.dto

import androidx.annotation.Keep

@Keep
data class DecodeModelDto(
    var iv: String = "",
    var value: String = ""
)