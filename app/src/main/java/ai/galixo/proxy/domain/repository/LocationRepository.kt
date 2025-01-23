package ai.galixo.proxy.domain.repository

import ai.galixo.proxy.domain.model.LocationData
import ai.galixo.proxy.utils.Result

interface LocationRepository {
    suspend fun fetchLocationInfo(): Result<LocationData>
}