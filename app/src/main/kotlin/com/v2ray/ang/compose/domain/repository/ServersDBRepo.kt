package com.v2ray.ang.compose.domain.repository

import com.v2ray.ang.compose.data.local.entities.ServerEntity

interface ServersDBRepo {

    suspend fun insertServerList(servers: List<ServerEntity>)

    suspend fun insertServer(server: ServerEntity): Boolean

    suspend fun selectedServer(server: ServerEntity): ServerEntity?

    suspend fun checkIfServerExists(serverId: Int): Boolean

    suspend fun updateServer(server: ServerEntity): Boolean

    suspend fun getAllServers(): List<ServerEntity>

    suspend fun deleteServerById(serverId: Int)
}