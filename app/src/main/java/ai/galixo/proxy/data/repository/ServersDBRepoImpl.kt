package ai.galixo.proxy.data.repository

import ai.galixo.proxy.data.local.ServersDatabase
import ai.galixo.proxy.data.local.dao.ServerDao
import ai.galixo.proxy.data.local.entities.ServerEntity
import ai.galixo.proxy.domain.repository.ServersDBRepo

class ServersDBRepoImpl(database: ServersDatabase) : ServersDBRepo {
    override suspend fun insertServerList(servers: List<ServerEntity>) {
        serverDao.insertServerList(servers)
    }

    override suspend fun insertServer(server: ServerEntity): Boolean {
        return serverDao.insertServer(server) != -1L
    }

    override suspend fun selectedServer(server: ServerEntity): ServerEntity? {
        return null
    }

    override suspend fun checkIfServerExists(serverId: Int): Boolean {
        return serverDao.checkIfServerExists(serverId) != null
    }

    override suspend fun updateServer(server: ServerEntity): Boolean {
        return serverDao.updateServer(server) > 0
    }

    override suspend fun getAllServers(): List<ServerEntity> {
        return serverDao.getAllServers()
    }

    override suspend fun deleteServerById(serverId: Int) {
        serverDao.deleteServerByID(serverId)
    }

    private val serverDao: ServerDao = database.serverDao
}