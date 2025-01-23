package ai.galixo.proxy.data.remote

import ai.galixo.proxy.domain.model.LocationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface LocationApi {

    @GET
    suspend fun getLocationInfo(
        @Url url: String
    ): Response<LocationResponse>
}
