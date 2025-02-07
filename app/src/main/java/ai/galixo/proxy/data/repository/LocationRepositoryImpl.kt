package ai.galixo.proxy.data.repository

import ai.galixo.proxy.R
import ai.galixo.proxy.data.remote.LocationApi
import ai.galixo.proxy.domain.model.LocationData
import ai.galixo.proxy.domain.repository.LocationRepository
import ai.galixo.proxy.utils.LOCATION_URL
import ai.galixo.proxy.utils.Result
import ai.galixo.proxy.utils.extensions.isOnline
import android.content.Context
import com.github.shadowsocks.bg.BaseService
import com.github.shadowsocks.preference.PrefUtils
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val context: Context,
    private val locationApi: LocationApi
) : LocationRepository {

    override suspend fun getUserCurrentLocation(): Flow<Result<LocationData>> = callbackFlow {
        trySend(Result.Loading(""))
        if (BaseService.isStarted) {
            trySend(Result.Success(getConnectedServer()))
        } else {
            trySend(fetchLocationInfo())
        }
        awaitClose()
    }

    private fun getConnectedServer(): LocationData {
        return PrefUtils.selectedProfile?.let {
            LocationData(
                locationName = "${it.cityName},${it.countryName}",
                ipAddress = it.ipaddress,
                timeZone = it.timezone,
                flag = it.flag,
                latitude = it.lat.toDouble(),
                longitude = it.lng.toDouble()
            )
        } ?: LocationData()
    }

    private suspend fun fetchLocationInfo(): Result<LocationData> {
        return try {
            if (context.isOnline()) {
                val response = locationApi.getLocationInfo(LOCATION_URL)
                Timber.d("LocationData: response = ${response.body()}")

                if (response.isSuccessful) {
                    val locationResponse = response.body()
                    locationResponse?.let {

                        val (latitude, longitude) = it.loc?.split(",").let {
                            it?.get(0)?.toDouble() to it?.get(1)?.toDouble()
                        }

                        Result.Success(
                            LocationData(
                                locationName = "${it.city},${it.country}",
                                ipAddress = it.ip ?: "N/A",
                                timeZone = it.timezone ?: "N/A",
                                flag = it.country?.lowercase() ?: "en",
                                latitude = latitude,
                                longitude = longitude
                            )
                        )
                    } ?: Result.Error(context.getString(R.string.failed_to_load_location_data))
                } else {
                    // If the response is not successful, handle failure
                    Result.Error("Error: ${response.code()} - ${response.message()}")
                }
            } else {
                Result.Error(context.getString(R.string.no_internet_connection))
            }
        } catch (e: Exception) {
            // Catching network errors or other issues
            Result.Error("Network error: ${e.message}")
        }
    }
}