package com.galixo.proxy.compose.domain.repository

import com.galixo.proxy.compose.domain.model.AppInfo
import kotlinx.coroutines.flow.Flow
import com.galixo.proxy.compose.utils.Result

interface AppsRepository {
    suspend fun getAppsList(): Flow<Result<ArrayList<AppInfo>>>
}