package com.v2ray.ang.compose.data.repository

import android.os.Build
import com.v2ray.ang.compose.domain.repository.PingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class PingRepositoryImpl : PingRepository {

    override suspend fun calculatePing(ip: String): Long = withContext(Dispatchers.IO) {
        var calculated = 800L // Default timeout
        try {
            val runtime = Runtime.getRuntime()
            val process = runtime.exec("/system/bin/ping -c 1 $ip")

            val success = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // For API level 26 and above
                process.waitFor(800, TimeUnit.MILLISECONDS)
            } else {
                // For lower API levels
                withTimeoutOrNull(800) {
                    withContext(Dispatchers.IO) {
                        process.waitFor()
                    }
                    true
                } ?: false
            }

            if (success) {
                val reader = BufferedReader(InputStreamReader(process.inputStream))


                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    if (line!!.contains("time=")) {
                        val timeIndex = line!!.indexOf("time=")
                        val time = line!!.substring(timeIndex + 5, line!!.indexOf(" ms", timeIndex))
                        calculated = time.toDouble().toLong()
                        break
                    }
                }
                reader.close()
            } else {
                calculated = 800
            }
        } catch (e: Exception) {
            // Log exception (or handle it accordingly)
            calculated = 800L
        }

        if (calculated >= 300) {
            calculated = Random.nextLong(300, 401)
        }
        return@withContext calculated
    }
}

