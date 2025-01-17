package com.github.shadowsocks.utils

import androidx.annotation.Keep

@Keep
data class DataTransferModel(
    val download: Long = 0L,
    val upload: Long = 0L
)