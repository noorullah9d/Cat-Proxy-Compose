package com.v2ray.ang.compose.ui.base

import androidx.annotation.Keep

@Keep
data class UiState<T>(
    val loading: Boolean = false,
    val error: Throwable? = null,
    val data: T? = null
)