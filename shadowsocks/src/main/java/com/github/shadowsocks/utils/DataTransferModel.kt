package com.github.shadowsocks.utils

import androidx.annotation.Keep

@Keep
data class DataTransferModel(
    var download: Long = 0L,
    var upload: Long = 0L
)