package com.galixo.proxy.compose.domain.model

enum class ConnectionStatus {
    NEW_CONNECTION,
    CONNECTED,
    DISCONNECTED,
    ERROR,
    CONNECTING,
    UNKNOWN,
    STOP_CONNECTION,
    RECONNECT,
}
