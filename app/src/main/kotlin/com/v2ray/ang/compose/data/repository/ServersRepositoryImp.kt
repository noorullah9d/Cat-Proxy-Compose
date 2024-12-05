package com.v2ray.ang.compose.data.repository

import android.content.Context
import com.v2ray.ang.compose.domain.ServerModel
import com.v2ray.ang.compose.domain.repository.ServersRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import com.v2ray.ang.compose.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ServersRepositoryImp @Inject constructor(
    private val context: Context
) : ServersRepository {

    override suspend fun getServers(): Flow<Result<List<ServerModel>>> {
        return flow {
            try {
                // Emit Loading state
                emit(Result.Loading)

                // Simulate a network or data fetch with a delay
                delay(1000) // Simulate network delay

                // Emit Success state with dummy data
                val serverList = listOf(
                    ServerModel(id = 1, countryName = "USA", cityName = "New York", ipaddress = "192.168.1.1", flag = "us"),
                    ServerModel(id = 2, countryName = "Germany", cityName = "Berlin", ipaddress = "192.168.1.2", flag = "de"),
                    ServerModel(id = 3, countryName = "France", cityName = "Paris", ipaddress = "192.168.1.3", flag = "fr")
                )
                emit(Result.Success(serverList))

            } catch (e: Exception) {
                // Emit Error state in case of failure
                emit(Result.Error(e.toString()))
            }
        }
    }
}
