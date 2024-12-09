package com.v2ray.ang.compose.domain.repository

import com.v2ray.ang.compose.domain.model.ServerModel
import com.v2ray.ang.compose.utils.Result
import kotlinx.coroutines.flow.Flow

interface ServersRepository {

    suspend fun getServers(hardRefresh: Boolean = false)

    fun serversList(): Flow<Result<List<ServerModel>>>

    fun getSelectedServer(): Flow<ServerModel?>

    fun isServerSelected(): Boolean

    fun setSelectedServer(serverModel: ServerModel)

    fun getLastConnectedServer(): ServerModel?

    fun setLastConnectedServer(serverModel: ServerModel)
}


