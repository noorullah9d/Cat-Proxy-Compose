package com.v2ray.ang.compose.domain.repository

import com.v2ray.ang.compose.domain.ServerModel
import com.v2ray.ang.compose.utils.Result
import kotlinx.coroutines.flow.Flow

interface ServersRepository {
    suspend fun getServers(): Flow<Result<List<ServerModel>>>
}


