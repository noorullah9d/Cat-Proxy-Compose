package com.v2ray.ang.compose.domain.repository

interface PingRepository {
    suspend fun calculatePing(ip: String): Long
}