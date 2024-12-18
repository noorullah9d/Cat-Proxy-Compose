package com.galixo.proxy.compose.data.remote

import com.galixo.proxy.compose.data.dto.MessageResponseDto
import com.galixo.proxy.compose.data.dto.SupportMessageDto
import com.galixo.proxy.compose.domain.model.ServerResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiService {

    @GET("api/servers")
    suspend fun getServersList(
        @Header("Authorization") authHeader: String
    ): Response<ServerResponse>

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