package ai.galixo.proxy.domain.repository

import ai.galixo.proxy.domain.model.AppInfo
import kotlinx.coroutines.flow.Flow
import ai.galixo.proxy.utils.Result

interface AppsRepository {
    suspend fun getAppsList(): Flow<Result<ArrayList<AppInfo>>>
}