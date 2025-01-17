package ai.galixo.proxy.domain.repository

import ai.galixo.proxy.utils.Result
import com.github.shadowsocks.preference.ServerModel
import kotlinx.coroutines.flow.Flow

interface ServersRepository {

    suspend fun getServers(hardRefresh: Boolean = false)

    fun serversList(): Flow<Result<List<ServerModel>>>

    fun getSelectedServer(): Flow<ServerModel?>

    fun isServerSelected(): Boolean

    fun getCurrentSelectedServer(): ServerModel?

    fun setSelectedServer(serverModel: ServerModel)

    fun getLastConnectedServer(): ServerModel?

    fun setLastConnectedServer(serverModel: ServerModel)
}


