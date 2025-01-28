package ai.galixo.proxy.data.remote

import ai.galixo.proxy.domain.model.ServersResponse
import ai.galixo.proxy.data.dto.MessageResponseDto
import ai.galixo.proxy.data.dto.SupportMessageDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiService {

    @GET("api/v1/servers")
    suspend fun getServersList(
        @Header("Authorization") authHeader: String
    ): Response<ServersResponse>

    /*@GET("api/servers")
    suspend fun getServersList(
        @Header("Authorization") authHeader: String
    ): Response<ServerResponse>*/

    @Headers(
        "Content-Type:application/json",
        "Access-Control-Request-Headers:*",
        "requesthost:proxy_app"
    )
    @POST
    suspend fun sendSupportMessage(
        @Url url: String,
        @Body body: SupportMessageDto
    ): Response<MessageResponseDto?>
}