package com.galixo.proxy.compose.domain.repository

interface PingRepository {
    suspend fun calculatePing(ip: String): Long
}