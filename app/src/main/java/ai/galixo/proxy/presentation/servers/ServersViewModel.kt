package ai.galixo.proxy.presentation.servers

import ai.galixo.proxy.domain.repository.ServersRepository
import ai.galixo.proxy.utils.GAME_FLAG
import ai.galixo.proxy.utils.Result
import ai.galixo.proxy.utils.SPORTS_FLAG
import ai.galixo.proxy.utils.STREAM_FLAG
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.shadowsocks.preference.ServerGroupModel
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

    private val _serverListState =
        MutableStateFlow<Result<List<ServerGroupModel>>>(Result.Loading("Loading..."))
    val serverListState: StateFlow<Result<List<ServerGroupModel>>> get() = _serverListState

    private var allServerGroups: List<ServerGroupModel> = listOf()
    private var recommendedServerGroups: List<ServerGroupModel> = listOf()

    init {
        // Start fetching the servers as soon as the view model is created
        collectServers()
    }

    private fun collectServers() = viewModelScope.launch {
        serversRepository.serversList().collect { result ->
            if (result is Result.Success) {
                allServerGroups = result.data
                recommendedServerGroups = getRecommendedServerGroups(result.data)
            }
            _serverListState.value = result
        }
    }

    // Group servers by country name for "All"
    fun getServerGroupsGroupedByCountry(): List<ServerGroupModel> {
        return allServerGroups
    }

    // Group servers by server type for "Recommended"
    fun getServerGroupsGroupedByServerType(servers: List<ServerGroupModel>): List<ServerGroupModel> {
        val gameServers = servers.filter { it.data.any { server -> server.serverType == "game" } }
        val streamServers = servers.filter { it.data.any { server -> server.serverType == "stream" } }
        val sportsServers = servers.filter { it.data.any { server -> server.serverType == "sports" } }

        return listOf(
            ServerGroupModel("Game", GAME_FLAG, ArrayList(gameServers.flatMap { it.data })),
            ServerGroupModel("Streaming", STREAM_FLAG, ArrayList(streamServers.flatMap { it.data })),
            ServerGroupModel("Sports", SPORTS_FLAG, ArrayList(sportsServers.flatMap { it.data }))
        )
    }

    /*fun filterServers(query: String) {
        val filteredGroups = if (query.isEmpty()) {
            allServerGroups
        } else {
            // Filter by country or server type
            allServerGroups.map { serverGroup ->
                // Filter servers within each group by name or type
                val filteredServers = serverGroup.data.filter {
                    it.countryName.contains(query, ignoreCase = true)
                }
                serverGroup.copy(data = ArrayList(filteredServers))
            }.filter { it.data.isNotEmpty() } // Remove groups with no servers matching the query
        }

        // Update the server list state with filtered groups
        _serverListState.value = Result.Success(filteredGroups)
    }*/
    fun filterServers(query: String, selectedTab: TabPage) {
        val filteredGroups = if (query.isEmpty()) {
            // Return groups based on the selected tab without filtering
            when (selectedTab) {
                TabPage.All -> allServerGroups
                TabPage.Recommended -> getServerGroupsGroupedByServerType(allServerGroups)
            }
        } else {
            // Apply filtering logic to search by server type or country
            val filteredByQuery = allServerGroups.map { serverGroup ->
                val filteredServers = serverGroup.data.filter {
                    it.countryName.contains(query, ignoreCase = true) /*|| it.serverType.contains(query, ignoreCase = true)*/
                }
                serverGroup.copy(data = ArrayList(filteredServers))
            }.filter { it.data.isNotEmpty() }

            // Return filtered groups
            when (selectedTab) {
                TabPage.All -> filteredByQuery
                TabPage.Recommended -> getServerGroupsGroupedByServerType(filteredByQuery)
            }
        }

        _serverListState.value = Result.Success(filteredGroups)
    }

    fun resetServers() {
        _serverListState.value = Result.Success(allServerGroups)
    }

    fun fetchServers() {
        viewModelScope.launch {
            serversRepository.getServers(hardRefresh = true)
        }
    }

    private fun getRecommendedServerGroups(servers: List<ServerGroupModel>): List<ServerGroupModel> {
        val groupedServers = servers.flatMap { it.data }
        return groupedServers.groupBy { it.serverType }
            .map { (serverType, serversOfType) ->
                ServerGroupModel(
                    countryName = serverType,
                    flag = serversOfType.firstOrNull()?.flag ?: "",
                    data = ArrayList(serversOfType)
                )
            }
    }

    fun setSelectedServer(serverModel: ServerModel) {
        serversRepository.setSelectedServer(serverModel)
    }
}
