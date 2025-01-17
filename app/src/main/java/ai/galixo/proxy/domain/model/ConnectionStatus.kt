package ai.galixo.proxy.domain.model

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
