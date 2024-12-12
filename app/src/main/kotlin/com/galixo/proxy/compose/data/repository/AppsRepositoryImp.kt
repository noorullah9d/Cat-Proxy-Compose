package com.galixo.proxy.compose.data.repository

import android.content.Context
import android.content.pm.PackageManager
import com.galixo.proxy.compose.domain.model.AppInfo
import com.galixo.proxy.compose.domain.repository.AppsRepository
import com.galixo.proxy.compose.utils.PrefUtils
import com.galixo.proxy.compose.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppsRepositoryImp @Inject constructor(
    private val context: Context
) : AppsRepository {
    override suspend fun getAppsList(): Flow<Result<ArrayList<AppInfo>>> = callbackFlow {
        trySend(Result.Loading(""))
        trySend(Result.success(getAllInstalledApps()))
        awaitClose()
    }

    private suspend fun getAllInstalledApps(): ArrayList<AppInfo> = withContext(Dispatchers.IO) {
        val packageManager = context.packageManager
        val packagesInfo = arrayListOf<AppInfo>()

        val packageList = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        for (packageInfo in packageList) {
            try {
                // Exclude system apps
                if (packageManager.getLaunchIntentForPackage(packageInfo.packageName) != null && !packageManager.getLaunchIntentForPackage(
                        packageInfo.packageName
                    )!!.equals("")
                ) {
                    val appName = packageInfo.loadLabel(packageManager).toString()
                    val packageName = packageInfo.packageName
                    val appIcon = packageInfo.loadIcon(packageManager)

                    if (packageName != context.packageName) {
                        val appModel = AppInfo(
                            packageName,
                            appName,
                            appIcon,
                            PrefUtils.isAppAllowed(packageName)
                        )
                        packagesInfo.add(appModel)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return@withContext packagesInfo
    }
}