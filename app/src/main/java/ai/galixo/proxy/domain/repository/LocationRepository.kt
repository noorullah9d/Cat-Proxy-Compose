package ai.galixo.proxy.domain.repository

import ai.galixo.proxy.domain.model.LocationData
import ai.galixo.proxy.utils.Result
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun getUserCurrentLocation(): Flow<Result<LocationData>>
}