package ai.galixo.proxy.data.repository

import ai.galixo.cypherlib.NativeLib
import ai.galixo.proxy.R
import ai.galixo.proxy.data.dto.Server
import ai.galixo.proxy.data.local.entities.ServerEntity
import ai.galixo.proxy.data.mapper.toServerEntity
import ai.galixo.proxy.data.mapper.toServerModel
import ai.galixo.proxy.data.remote.ApiService
import ai.galixo.proxy.domain.repository.CypherRepository
import ai.galixo.proxy.domain.repository.PingRepository
import ai.galixo.proxy.domain.repository.ServersDBRepo
import ai.galixo.proxy.domain.repository.ServersRepository
import ai.galixo.proxy.utils.Result
import ai.galixo.proxy.utils.extensions.isOnline
import android.content.Context
import com.github.shadowsocks.bg.BaseService
import com.github.shadowsocks.extensions.formatTime
import com.github.shadowsocks.preference.PrefUtils
import com.github.shadowsocks.preference.PrefUtils.lastSelectedProfile
import com.github.shadowsocks.preference.PrefUtils.selectedProfile
import com.github.shadowsocks.preference.ServerGroupModel
import com.github.shadowsocks.preference.ServerModel
import com.github.shadowsocks.utils.DataTransferModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ServersRepositoryImp @Inject constructor(
    private val context: Context,
    private val apiService: ApiService,
    private val cypherRepository: CypherRepository,
    private val pingRepository: PingRepository,
    private val serverDBRepo: ServersDBRepo
) : ServersRepository {

    //    private val _serverListState = MutableStateFlow<Result<List<ServerModel>>>(Result.Loading("Loading..."))
    private val _serverListState = MutableStateFlow<Result<List<ServerGroupModel>>>(Result.Loading("Loading..."))
    private val _selectedServer = MutableStateFlow<ServerModel?>(null)

    private var fastestServer: ServerModel? = null
    private var serversFetchingCalled = false

    override suspend fun getServers(hardRefresh: Boolean) {
        val initialLocalList = serverDBRepo.getAllServers()
        Timber.d("Servers: local: $initialLocalList")
        if (!context.isOnline()) {
            handleServerFetchFailure(initialLocalList)
            return
        }

        if (serversFetchingCalled && !hardRefresh) {
            return
        }
        serversFetchingCalled = true
        _serverListState.value = Result.Loading("Loading...")
        Timber.d("Servers: Loading...")

        runCatching {
            val token = cypherRepository.getShift(NativeLib.getAuthorizationHeader())
            val serverResponse = apiService.getServersList(token)

            if (serverResponse.isSuccessful && serverResponse.body() != null) {
                val serversList = serverResponse.body()!!.servers.flatMap { it.data }
                Timber.d("Servers: list=$serversList")

                val decryptedList = addOrUpdateEntities(serversList)
                if (decryptedList.isNotEmpty()) {
                    updateSelectedServer()
                }
                _serverListState.value = Result.Success(decryptedList)
                Timber.d("Servers: getServers: $decryptedList")
            } else {
                handleServerFetchFailure(initialLocalList)
            }
        }.onFailure { e ->
            handleServerFetchFailure(initialLocalList)
            Timber.e("Servers: error= $e")
        }
    }

    //    override fun serversList(): Flow<Result<List<ServerModel>>> = _serverListState
    override fun serversList(): Flow<Result<List<ServerGroupModel>>> = _serverListState

    override fun isServerSelected(): Boolean {
        return selectedProfile != null
    }

    override fun getCurrentSelectedServer(): ServerModel? {
        return selectedProfile
    }

    override fun setSelectedServer(serverModel: ServerModel) {
        selectedProfile = serverModel
        _selectedServer.value = selectedProfile
    }

    override fun getSelectedServer(): Flow<ServerModel?> = _selectedServer

    override fun getLastConnectedServer(): ServerModel? {
        return lastSelectedProfile
    }

    override fun setLastConnectedServer(serverModel: ServerModel) {
        lastSelectedProfile = serverModel
    }

    override fun getElapsedTime(): String = formatTime(BaseService.elapsedTime)

    override fun getDataUsed(): DataTransferModel = BaseService.totalDataTransferModel

    private suspend fun handleServerFetchFailure(initialLocalList: List<ServerEntity>) {
        if (initialLocalList.isEmpty()) {
            _serverListState.value = Result.Error(context.getString(R.string.server_not_found))
        } else {
            serversFetchingCalled = false
            val decryptedList = calculatePingAndDecryptText(initialLocalList)
            _serverListState.value = Result.Success(decryptedList)
            updateSelectedServer()
        }
    }

    private suspend fun addOrUpdateEntities(serversList: List<Server>): List<ServerGroupModel> {
        val entities = serversList.map { it.toServerEntity() }
        entities.forEach { entity ->
            if (serverDBRepo.checkIfServerExists(entity.id)) {
                serverDBRepo.updateServer(entity)
            } else {
                serverDBRepo.insertServer(entity)
            }
        }
        return removeIfAnyServerIsNotAvailable(entities)
    }

    private suspend fun removeIfAnyServerIsNotAvailable(serversList: List<ServerEntity>): List<ServerGroupModel> {
        val localServers = serverDBRepo.getAllServers()
        localServers.forEach {
            if (!serversList.contains(it)) {
                serverDBRepo.deleteServerById(it.id)
            }
        }
        return calculatePingAndDecryptText(serverDBRepo.getAllServers())
    }

    private suspend fun calculatePingAndDecryptText(serversList: List<ServerEntity>): List<ServerGroupModel> {
        val serversDecryptedList = serversList.map { it.toServerModel() }
        coroutineScope {
            serversDecryptedList.chunked(4)
                .forEach { chunk ->  // Changed to process in smaller chunks
                    chunk.forEach { serverModel ->
                        launch {
                            performDecryption(serverModel)
                        }
                    }
                }
        }

        // Check the isAdsRemoved flag from SharedPreferences
        val isAdsRemoved = PrefUtils.isAdsRemoved

        val updatedServers = if (isAdsRemoved) {
            // If ads are removed, set isPremium to true for all servers
            serversDecryptedList.map { server ->
                server.copy(isPremium = false)
            }
        } else {
            // Update isPremium attribute based on the ping
            serversDecryptedList.map { server ->
                server.copy(isPremium = server.type == "premium")
            }
        }
        Timber.d("Servers: updatedServers: $updatedServers")

        // Separate free and premium servers
        val freeServers = updatedServers.filter { it.type == "free" }.sortedBy { it.ping }
        val premiumServers = updatedServers.filter { it.type == "premium" }

        // Find the server with the smallest ping
        fastestServer = freeServers.firstOrNull()
        Timber.d("Servers: fastestServer: $fastestServer")

        // Combine sorted free servers and premium servers
        return groupServersByCountry(freeServers + premiumServers)
    }

    private suspend fun groupServersByCountry(serverList: List<ServerModel>): List<ServerGroupModel> =
        withContext(Dispatchers.IO) {
            // Group the servers by country name
            val groupedByCountry = serverList.groupBy { it.countryName }

            return@withContext groupedByCountry.map { (countryName, serversList) ->
                // Create a ServerGroupModel for each country
                ServerGroupModel(
                    countryName = countryName,
                    flag = serversList.firstOrNull()?.flag ?: "",  // Get the flag from the first server
                    data = ArrayList(serversList)  // Add all servers of the same country
                )
            }
        }

    private suspend fun performDecryption(serverModel: ServerModel) = withContext(Dispatchers.IO) {
        serverModel.password = cypherRepository.decryptAES256CBC(serverModel.password)
        serverModel.ipaddress = cypherRepository.decryptAES256CBC(serverModel.ipaddress)
        serverModel.port = cypherRepository.decryptAES256CBC(serverModel.port)
        serverModel.encryption = cypherRepository.decryptAES256CBC(serverModel.encryption)
        serverModel.ping = pingRepository.calculatePing(serverModel.ipaddress)
    }

    private fun updateSelectedServer() {
        /*if (!BaseService.isStarted)*/ selectedProfile = fastestServer
        _selectedServer.value = selectedProfile
    }
}
