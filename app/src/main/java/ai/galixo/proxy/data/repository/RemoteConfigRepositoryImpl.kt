package ai.galixo.proxy.data.repository

import ai.galixo.proxy.BuildConfig
import ai.galixo.proxy.R
import ai.galixo.proxy.admob.admob_app_open_id
import ai.galixo.proxy.admob.admob_banner_id_connected
import ai.galixo.proxy.admob.admob_banner_id_home
import ai.galixo.proxy.admob.admob_banner_id_servers
import ai.galixo.proxy.admob.admob_interstitial_id_connection
import ai.galixo.proxy.admob.admob_interstitial_id_disconnect
import ai.galixo.proxy.admob.admob_interstitial_id_splash
import ai.galixo.proxy.admob.admob_interstitial_id_timer
import ai.galixo.proxy.admob.admob_native_id_exit
import ai.galixo.proxy.admob.admob_native_id_home
import ai.galixo.proxy.admob.admob_native_id_languages
import ai.galixo.proxy.admob.admob_rewarded1_ad_id
import ai.galixo.proxy.admob.admob_rewarded2_ad_id
import ai.galixo.proxy.admob.admob_rewarded3_ad_id
import ai.galixo.proxy.admob.freeTrialKey
import ai.galixo.proxy.domain.repository.RemoteConfigRepository
import android.content.Context
import com.github.shadowsocks.preference.PrefUtils
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.google.gson.Gson
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import ai.galixo.proxy.domain.model.AdIds

class RemoteConfigRepositoryImpl(
    private val context: Context,
    private val remoteConfig: FirebaseRemoteConfig
) : RemoteConfigRepository {
    override suspend fun getRemoteResponse() {
        try {
            setDefaultIds()
            val configSettings = remoteConfigSettings {
                minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) {
                    0L
                } else {
                    43200L
                }
            }

            remoteConfig.setConfigSettingsAsync(configSettings)
            remoteConfig.fetchAndActivate().await()
            /*remoteConfig.fetchAndActivate()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("RemoteConfig:"," Fetch and activate succeeded")

                        val response = remoteConfig.getString("ad_release_25_1_2_4")
                        Log.d("RemoteConfig:"," test response: $response")
                    } else {
                        Log.e("RemoteConfig:"," Fetch failed")
                    }
                }*/

            if (BuildConfig.DEBUG) {
                val responseIds = remoteConfig.getString("ad_debug_26_1_2_5")
                Timber.d("RemoteConfig: debug ad ids: $responseIds")
                val adIds = Gson().fromJson(responseIds, AdIds::class.java)
                if (responseIds.isEmpty()) {
                    return
                }

                admob_interstitial_id_connection =
                    if (adIds.interstitialConnection?.show == true && adIds.interstitialConnection?.adId?.isNotEmpty() == true) {
                        adIds?.interstitialConnection?.adId ?: ""
                    } else {
                        ""
                    }

                admob_interstitial_id_disconnect =
                    if (adIds.interstitialDisconnect?.show == true && adIds.interstitialDisconnect?.adId?.isNotEmpty() == true) {
                        adIds?.interstitialDisconnect?.adId ?: ""
                    } else {
                        ""
                    }

                admob_interstitial_id_timer =
                    if (adIds.interstitialTimer?.show == true && adIds.interstitialTimer?.adId?.isNotEmpty() == true) {
                        adIds?.interstitialTimer?.adId ?: ""
                    } else {
                        ""
                    }

                admob_interstitial_id_splash =
                    if (adIds.interstitialSplash?.show == true && adIds.interstitialSplash?.adId?.isNotEmpty() == true) {
                        PrefUtils.isSplashInterstitialAllowedFromRemoteConfig = true
                        adIds?.interstitialSplash?.adId ?: ""
                    } else {
                        PrefUtils.isSplashInterstitialAllowedFromRemoteConfig = false
                        ""
                    }

                admob_banner_id_home =
                    if (adIds.bannerHome?.show == true && adIds.bannerHome?.adId?.isNotEmpty() == true) {
                        adIds?.bannerHome?.adId ?: ""
                    } else {
                        ""
                    }

                admob_banner_id_servers =
                    if (adIds.bannerServers?.show == true && adIds.bannerServers?.adId?.isNotEmpty() == true) {
                        adIds?.bannerServers?.adId ?: ""
                    } else {
                        ""
                    }

                admob_banner_id_connected =
                    if (adIds.bannerConnected?.show == true && adIds.bannerConnected?.adId?.isNotEmpty() == true) {
                        adIds?.bannerConnected?.adId ?: ""
                    } else {
                        ""
                    }

                admob_native_id_exit =
                    if (adIds.nativeExit?.show == true && adIds.nativeExit?.adId?.isNotEmpty() == true) {
                        adIds?.nativeExit?.adId ?: ""
                    } else {
                        ""
                    }

                admob_native_id_languages =
                    if (adIds.nativeLanguages?.show == true && adIds.nativeLanguages?.adId?.isNotEmpty() == true) {
                        adIds?.nativeLanguages?.adId ?: ""
                    } else {
                        ""
                    }

                admob_native_id_home =
                    if (adIds.nativeHome?.show == true && adIds.nativeHome?.adId?.isNotEmpty() == true) {
                        adIds?.nativeHome?.adId ?: ""
                    } else {
                        ""
                    }

                admob_app_open_id =
                    if (adIds.appOpen?.show == true && adIds.appOpen?.adId?.isNotEmpty() == true) {
                        PrefUtils.isAppOpenAllowedFromRemoteConfig = true
                        adIds?.appOpen?.adId ?: ""
                    } else {
                        PrefUtils.isAppOpenAllowedFromRemoteConfig = false
                        ""
                    }

                admob_rewarded1_ad_id =
                    if (adIds.rewardedAd1?.show == true && adIds.rewardedAd1?.adId?.isNotEmpty() == true) {
                        adIds?.rewardedAd1?.adId ?: ""
                    } else {
                        ""
                    }

                admob_rewarded2_ad_id =
                    if (adIds.rewardedAd2?.show == true && adIds.rewardedAd2?.adId?.isNotEmpty() == true) {
                        adIds?.rewardedAd2?.adId ?: ""
                    } else {
                        ""
                    }

                admob_rewarded3_ad_id =
                    if (adIds.rewardedAd3?.show == true && adIds.rewardedAd3?.adId?.isNotEmpty() == true) {
                        adIds?.rewardedAd3?.adId ?: ""
                    } else {
                        ""
                    }

                freeTrialKey = adIds?.freeTrialKey?.priority ?: 0

            } else {
                val responseIds = remoteConfig.getString("ad_release_26_1_2_5")
                Timber.d("RemoteConfig: release ad ids: $responseIds")
                val adIds = Gson().fromJson(responseIds, AdIds::class.java)
                if (responseIds.isEmpty()) {
                    return
                }

                admob_interstitial_id_connection =
                    if (adIds.interstitialConnection?.show == true && adIds.interstitialConnection?.adId?.isNotEmpty() == true) {
                        adIds?.interstitialConnection?.adId ?: ""
                    } else {
                        ""
                    }

                admob_interstitial_id_disconnect =
                    if (adIds.interstitialDisconnect?.show == true && adIds.interstitialDisconnect?.adId?.isNotEmpty() == true) {
                        adIds?.interstitialDisconnect?.adId ?: ""
                    } else {
                        ""
                    }

                admob_interstitial_id_timer =
                    if (adIds.interstitialTimer?.show == true && adIds.interstitialTimer?.adId?.isNotEmpty() == true) {
                        adIds?.interstitialTimer?.adId ?: ""
                    } else {
                        ""
                    }

                admob_interstitial_id_splash =
                    if (adIds.interstitialSplash?.show == true && adIds.interstitialSplash?.adId?.isNotEmpty() == true) {
                        PrefUtils.isSplashInterstitialAllowedFromRemoteConfig = true
                        adIds?.interstitialSplash?.adId ?: ""
                    } else {
                        PrefUtils.isSplashInterstitialAllowedFromRemoteConfig = false
                        ""
                    }

                admob_banner_id_home =
                    if (adIds.bannerHome?.show == true && adIds.bannerHome?.adId?.isNotEmpty() == true) {
                        adIds?.bannerHome?.adId ?: ""
                    } else {
                        ""
                    }

                admob_banner_id_servers =
                    if (adIds.bannerServers?.show == true && adIds.bannerServers?.adId?.isNotEmpty() == true) {
                        adIds?.bannerServers?.adId ?: ""
                    } else {
                        ""
                    }

                admob_banner_id_connected =
                    if (adIds.bannerConnected?.show == true && adIds.bannerConnected?.adId?.isNotEmpty() == true) {
                        adIds?.bannerConnected?.adId ?: ""
                    } else {
                        ""
                    }

                admob_native_id_exit =
                    if (adIds.nativeExit?.show == true && adIds.nativeExit?.adId?.isNotEmpty() == true) {
                        adIds?.nativeExit?.adId ?: ""
                    } else {
                        ""
                    }

                admob_native_id_languages =
                    if (adIds.nativeLanguages?.show == true && adIds.nativeLanguages?.adId?.isNotEmpty() == true) {
                        adIds?.nativeLanguages?.adId ?: ""
                    } else {
                        ""
                    }

                admob_native_id_home =
                    if (adIds.nativeHome?.show == true && adIds.nativeHome?.adId?.isNotEmpty() == true) {
                        adIds?.nativeHome?.adId ?: ""
                    } else {
                        ""
                    }

                admob_app_open_id =
                    if (adIds.appOpen?.show == true && adIds.appOpen?.adId?.isNotEmpty() == true) {
                        PrefUtils.isAppOpenAllowedFromRemoteConfig = true
                        adIds?.appOpen?.adId ?: ""
                    } else {
                        PrefUtils.isAppOpenAllowedFromRemoteConfig = false
                        ""
                    }

                admob_rewarded1_ad_id =
                    if (adIds.rewardedAd1?.show == true && adIds.rewardedAd1?.adId?.isNotEmpty() == true) {
                        adIds?.rewardedAd1?.adId ?: ""
                    } else {
                        ""
                    }

                admob_rewarded2_ad_id =
                    if (adIds.rewardedAd2?.show == true && adIds.rewardedAd2?.adId?.isNotEmpty() == true) {
                        adIds?.rewardedAd2?.adId ?: ""
                    } else {
                        ""
                    }

                admob_rewarded3_ad_id =
                    if (adIds.rewardedAd3?.show == true && adIds.rewardedAd3?.adId?.isNotEmpty() == true) {
                        adIds?.rewardedAd3?.adId ?: ""
                    } else {
                        ""
                    }

                freeTrialKey = adIds?.freeTrialKey?.priority ?: 0
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun setDefaultIds() {
        admob_interstitial_id_connection =
            context.getString(R.string.admob_interstitial_id_connection)
        admob_interstitial_id_disconnect =
            context.getString(R.string.admob_interstitial_id_disconnect)
        admob_interstitial_id_timer = context.getString(R.string.admob_interstitial_id_timer)
        admob_interstitial_id_splash = context.getString(R.string.admob_interstitial_id_splash)
        admob_banner_id_home = context.getString(R.string.admob_banner_id_home)
        admob_banner_id_servers = context.getString(R.string.admob_banner_id_servers)
        admob_banner_id_connected = context.getString(R.string.admob_banner_id_connected)
        admob_native_id_exit = context.getString(R.string.admob_native_id_exit)
        admob_native_id_languages = context.getString(R.string.admob_native_id_languages)
        admob_native_id_home = context.getString(R.string.admob_native_id_home)
        admob_app_open_id = context.getString(R.string.admob_app_open_id)
        admob_rewarded1_ad_id = context.getString(R.string.admob_rewarded1_ad_id)
        admob_rewarded2_ad_id = context.getString(R.string.admob_rewarded2_ad_id)
        admob_rewarded3_ad_id = context.getString(R.string.admob_rewarded3_ad_id)
    }
}