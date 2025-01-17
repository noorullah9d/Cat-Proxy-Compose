package ai.galixo.proxy.domain.repository

interface PingRepository {
    suspend fun calculatePing(ip: String): Long
}