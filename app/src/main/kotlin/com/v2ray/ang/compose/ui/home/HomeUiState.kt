package com.v2ray.ang.compose.ui.home

import com.v2ray.ang.compose.domain.model.ServerModel

sealed class HomeUiState {
    data class ServerSelected(val serverModel: ServerModel?) : HomeUiState()
    object Connecting : HomeUiState()
    object Disconnected : HomeUiState()
    object ConnectionFailed : HomeUiState()
    object Connected : HomeUiState()
    data class Time(val connectionTime: String) : HomeUiState()
    data class IpAddress(val ipAddress: String) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}