package com.galixo.proxy.compose.data.repository

import android.content.Context
import android.os.Build
import com.galixo.proxy.R
import com.galixo.proxy.compose.data.dto.Data
import com.galixo.proxy.compose.data.dto.SupportMessageDto
import com.galixo.proxy.compose.data.remote.ApiService
import com.galixo.proxy.compose.domain.repository.FeedbackRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FeedbackRepositoryImp @Inject constructor(
    private val context: Context,
    private val apiService: ApiService
) : FeedbackRepository {
    override suspend fun sendFeedBack(
        message: String,
        callback: (Boolean) -> Unit
    ) {
        try {
            withContext(Dispatchers.IO) {
                val reqString =
                    (Build.MANUFACTURER + " " + Build.MODEL + " " + Build.VERSION.RELEASE + " " + Build.VERSION_CODES::class.java.fields[Build.VERSION.SDK_INT].name)
                val result =
                    apiService.sendSupportMessage(
                        context.getString(R.string.support_url),
                        SupportMessageDto(Data(reqString, "free", message))
                    )
                if (result.isSuccessful && result.body() != null) {
                    withContext(Dispatchers.Main) {
                        callback.invoke(true)
                    }

                } else {
                    withContext(Dispatchers.Main) {
                        callback.invoke(false)
                    }
                }
            }
        } catch (_: Exception) {
            withContext(Dispatchers.Main) {
                callback.invoke(false)
            }
        }
    }
}