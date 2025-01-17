package ai.galixo.proxy.presentation.home

import ai.galixo.proxy.data.helper.ConnectionTime
import ai.galixo.proxy.domain.model.ConnectionStatus
import ai.galixo.proxy.domain.model.Stats
import ai.galixo.proxy.domain.repository.ServersRepository
import ai.galixo.proxy.presentation.base.BaseViewModel
import androidx.lifecycle.viewModelScope
import com.github.shadowsocks.preference.ServerModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val serversRepository: ServersRepository
) : BaseViewModel<HomeUiState>() {

    private val connectionStatusStateFlow: MutableStateFlow<ConnectionStatus> =
        MutableStateFlow(ConnectionStatus.UNKNOWN)

    private val statsStateFlow: MutableStateFlow<Stats> =
        MutableStateFlow(Stats("00 : 00 : 00"))

    val connectionStatus: StateFlow<ConnectionStatus> = connectionStatusStateFlow.stateIn(
        viewModelScope,
        SharingStarted.Companion.WhileSubscribed(5000L),
        ConnectionStatus.DISCONNECTED
    )

    val selectedServer: StateFlow<ServerModel?> = serversRepository.getSelectedServer()
        .stateIn(
            viewModelScope,
            SharingStarted.Companion.Lazily,
            null
        )

    private var statusJob: Job? = null
    private var statsJob: Job? = null

    fun reconnect() = viewModelScope.launch {
        connectionStatusStateFlow.emit(ConnectionStatus.RECONNECT)
    }

    fun stopConnection() = viewModelScope.launch {
        connectionStatusStateFlow.emit(ConnectionStatus.STOP_CONNECTION)
    }

    fun updateState(connectionState: ConnectionStatus) {
        connectionStatusStateFlow.tryEmit(connectionState)
    }

    fun setPreConnectionStatus(status: ConnectionStatus) = viewModelScope.launch {
        when (status) {
            ConnectionStatus.CONNECTING -> {
                connectionStatusStateFlow.emit(ConnectionStatus.CONNECTING)
            }

            ConnectionStatus.CONNECTED -> {
                connectionStatusStateFlow.emit(ConnectionStatus.CONNECTED)
            }

            ConnectionStatus.DISCONNECTED -> {
                connectionStatusStateFlow.emit(ConnectionStatus.DISCONNECTED)
            }

            else -> {}
        }
    }

    fun getStatus() {
        statusJob?.cancel()
        statusJob = null
        statusJob = viewModelScope.launch {
            connectionStatus.collect {
                when (it) {
                    ConnectionStatus.CONNECTING -> {
                        updateUi(HomeUiState.Connecting)
                    }

                    ConnectionStatus.CONNECTED -> {
                        updateUi(HomeUiState.Connected)
                    }

                    ConnectionStatus.DISCONNECTED -> {
                        updateUi(HomeUiState.Disconnected)
                    }

                    ConnectionStatus.ERROR -> {
                        updateUi(HomeUiState.ConnectionFailed)
                    }

                    ConnectionStatus.UNKNOWN -> {
                        updateUi(HomeUiState.ConnectionFailed)
                    }

                    else -> {}
                }
            }
        }
    }

    fun getStats() {
        statsJob?.cancel()
        statsJob = null
        statsJob = viewModelScope.launch {
            statsStateFlow.collect {
                updateUi(
                    HomeUiState.Time(
                        it.connectionTime
                    )
                )
            }
        }
    }

    fun getPreConnectionStatus(status: (HomeUiState) -> Unit) = viewModelScope.launch {

        when (connectionStatusStateFlow.value) {

            ConnectionStatus.CONNECTING -> {
                status(HomeUiState.Connecting)
            }

            ConnectionStatus.CONNECTED, ConnectionStatus.STOP_CONNECTION -> {
                status(HomeUiState.Connected)
            }

            ConnectionStatus.DISCONNECTED, ConnectionStatus.UNKNOWN -> {
                status(HomeUiState.Disconnected)
            }

            else -> {}
        }
    }

    fun cancelJobs() {
        try {
            statsJob?.cancel()
            statusJob?.cancel()
            statsJob = null
            statusJob = null
        } catch (_: Exception) {
        }
    }

    fun updateTime(time: ConnectionTime) {
        statsStateFlow.tryEmit(Stats(time.time))
    }

    fun getCurrentSelectedServer(): ServerModel? {
        return serversRepository.getCurrentSelectedServer()
    }

    fun setLastConnectedServer(serverModel: ServerModel) {
        serversRepository.setLastConnectedServer(serverModel)
    }

    fun connect() {
        viewModelScope.launch {
            when (connectionStatus.value) {
                ConnectionStatus.CONNECTING, ConnectionStatus.CONNECTED -> {
                    connectionStatusStateFlow.emit(ConnectionStatus.STOP_CONNECTION)
                }

                ConnectionStatus.NEW_CONNECTION -> {
                    connectionStatusStateFlow.emit(ConnectionStatus.STOP_CONNECTION)
                }

                ConnectionStatus.DISCONNECTED, ConnectionStatus.UNKNOWN, ConnectionStatus.STOP_CONNECTION -> {
                    Timber.d("HomeScreen: emitting new connection...")
                    connectionStatusStateFlow.emit(
                        ConnectionStatus.NEW_CONNECTION
                    )
                }

                else -> {}
            }
        }
    }


    fun isServerSelected(): Boolean = serversRepository.isServerSelected()
}