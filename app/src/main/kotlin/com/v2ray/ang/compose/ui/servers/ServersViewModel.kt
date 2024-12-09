package com.v2ray.ang.compose.ui.servers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.v2ray.ang.compose.domain.model.ServerModel
import com.v2ray.ang.compose.domain.repository.ServersRepository
import com.v2ray.ang.compose.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServersViewModel @Inject constructor(
    private val serversRepository: ServersRepository
) : ViewModel() {

    private val _serverListState = MutableStateFlow<Result<List<ServerModel>>>(Result.Loading("Loading..."))
    val serverListState: StateFlow<Result<List<ServerModel>>> get() = _serverListState

    init {
        // Start fetching the servers as soon as the view model is created
        viewModelScope.launch {
            serversRepository.serversList().collect { result ->
                _serverListState.value = result
            }
        }
    }

    fun fetchServers() {
        viewModelScope.launch {
            serversRepository.getServers(hardRefresh = true)
        }
    }

    fun setSelectedServer(serverModel: ServerModel) {
        serversRepository.setSelectedServer(serverModel)
    }
}
