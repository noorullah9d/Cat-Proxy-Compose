package ai.galixo.proxy.domain.repository

interface RemoteConfigRepository {

    suspend fun getRemoteResponse()

    fun setDefaultIds()
}