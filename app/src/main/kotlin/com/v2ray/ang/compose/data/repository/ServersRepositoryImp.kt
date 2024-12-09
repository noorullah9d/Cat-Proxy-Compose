package com.v2ray.ang.compose.data.repository

import android.content.Context
import android.util.Log
import com.galixo.cypherlib.NativeLib
import com.v2ray.ang.R
import com.v2ray.ang.compose.data.dto.ServerDto
import com.v2ray.ang.compose.data.local.entities.ServerEntity
import com.v2ray.ang.compose.data.mapper.toServerEntity
import com.v2ray.ang.compose.data.mapper.toServerModel
import com.v2ray.ang.compose.data.remote.ApiService
import com.v2ray.ang.compose.domain.model.ServerModel
import com.v2ray.ang.compose.domain.repository.CypherRepository
import com.v2ray.ang.compose.domain.repository.PingRepository
import com.v2ray.ang.compose.domain.repository.ServersDBRepo
import com.v2ray.ang.compose.domain.repository.ServersRepository
import com.v2ray.ang.compose.extensions.isOnline
import com.v2ray.ang.compose.utils.PrefUtils.lastSelectedProfile
import com.v2ray.ang.compose.utils.PrefUtils.selectedProfile
import com.v2ray.ang.compose.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ServersRepositoryImp @Inject constructor(
    private val context: Context,
    private val apiService: ApiService,
    private val cypherRepository: CypherRepository,
    private val pingRepository: PingRepository,
    private val serverDBRepo: ServersDBRepo
) : ServersRepository {

    private val _serverListState =
        MutableStateFlow<Result<List<ServerModel>>>(Result.Loading("Loading..."))
    private val _selectedServer = MutableStateFlow<ServerModel?>(null)

    private var fastestServer: ServerModel? = null
    private var serversFetchingCalled = false

    override suspend fun getServers(hardRefresh: Boolean) {
        val initialLocalList = serverDBRepo.getAllServers()
        if (!context.isOnline()) {
            handleServerFetchFailure(initialLocalList)
            return
        }

        if (serversFetchingCalled && !hardRefresh) {
            return
        }
        serversFetchingCalled = true
        _serverListState.value = Result.Loading("Loading...")

        runCatching {
            val token = cypherRepository.getShift(NativeLib.getAuthorizationHeader())
            val serverResponse = apiService.getServersList(token)

            if (serverResponse.isSuccessful && serverResponse.body() != null) {
                val serversList = serverResponse.body()!!.servers.flatMap { it.data }
                val decryptedList = addOrUpdateEntities(serversList)
                if (decryptedList.isNotEmpty()) {
                    updateSelectedServer()
                }
                _serverListState.value = Result.Success(decryptedList)
            } else {
                handleServerFetchFailure(initialLocalList)
            }
        }.onFailure { e ->
            handleServerFetchFailure(initialLocalList)
            Log.e("Servers:", e.toString())
        }
    }

    override fun serversList(): Flow<Result<List<ServerModel>>> = _serverListState

    override fun isServerSelected(): Boolean {
        return selectedProfile != null
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

    private suspend fun handleServerFetchFailure(initialLocalList: List<ServerEntity>) {
        if (initialLocalList.isEmpty()) {
            _serverListState.value = Result.Error(context.getString(R.string.server_not_available))
        } else {
            serversFetchingCalled = false
            val decryptedList = calculatePingAndDecryptText(initialLocalList)
            _serverListState.value = Result.Success(decryptedList)
            updateSelectedServer()
        }
    }

    private suspend fun addOrUpdateEntities(serversList: List<ServerDto>): List<ServerModel> {
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

    private suspend fun removeIfAnyServerIsNotAvailable(serversList: List<ServerEntity>): List<ServerModel> {
        val localServers = serverDBRepo.getAllServers()
        localServers.forEach {
            if (!serversList.contains(it)) {
                serverDBRepo.deleteServerById(it.id)
            }
        }
        return calculatePingAndDecryptText(serverDBRepo.getAllServers())
    }

    private suspend fun calculatePingAndDecryptText(serversList: List<ServerEntity>): List<ServerModel> {
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
        val isAdsRemoved = /*PrefUtils.isAdsRemoved*/ false

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
        Log.d("Servers", " updatedServers: $updatedServers")

        // Separate free and premium servers
        val freeServers = updatedServers.filter { it.type == "free" }.sortedBy { it.ping }
        val premiumServers = updatedServers.filter { it.type == "premium" }

        // Find the server with the smallest ping
        fastestServer = freeServers.firstOrNull()
        Log.d("Servers", " fastestServer: $fastestServer")

        // Combine sorted free servers and premium servers
        return freeServers + premiumServers
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
