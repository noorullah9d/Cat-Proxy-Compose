package com.v2ray.ang.compose.ui.servers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.v2ray.ang.compose.domain.ServerModel
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

    private val _serverListState = MutableStateFlow<Result<List<ServerModel>>>(Result.Loading)
    val serverListState: StateFlow<Result<List<ServerModel>>> = _serverListState

    init {
        fetchServers()
    }

    private fun fetchServers() {
        // Collect from the repository's flow
        viewModelScope.launch {
            serversRepository.getServers()
                .collect { result ->
                    // Update state with the collected result
                    _serverListState.value = result
                }
        }
    }

    /*fun getServerById(selectedServerId: Int): ServerModel? {
        // Get the server list from the result, it should ideally come from your repository
        return when (val result = _serverListState.value) {
            is Result.Success -> {
                // Find the server by ID from the list
                result.data.find { it.id == selectedServerId }
            }
            else -> {
                null // Return null if the result is not a success or the list is empty
            }
        }
    }*/
}
