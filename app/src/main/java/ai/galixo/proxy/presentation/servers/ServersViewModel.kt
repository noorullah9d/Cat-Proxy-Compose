package ai.galixo.proxy.presentation.servers

import ai.galixo.proxy.domain.repository.ServersRepository
import ai.galixo.proxy.utils.Result
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.shadowsocks.preference.ServerModel
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

    private var allServers: ArrayList<ServerModel> = arrayListOf()

    init {
        // Start fetching the servers as soon as the view model is created
        collectServers()
    }

    private fun collectServers() = viewModelScope.launch {
        serversRepository.serversList().collect { result ->
            if (result is Result.Success) {
                allServers = result.data as ArrayList
            }
            _serverListState.value = result
        }
    }

    fun filterServers(query: String) {
        val filteredServers = if (query.isEmpty()) {
            allServers
        } else {
            allServers.filter {
                it.countryName.contains(query, ignoreCase = true) == true
            } as ArrayList<ServerModel>
        }
        _serverListState.value = Result.Success(filteredServers)
    }

    fun resetServers() {
        _serverListState.value = Result.Success(allServers)
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
