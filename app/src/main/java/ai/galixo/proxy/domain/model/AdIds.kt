package ai.galixo.proxy.domain.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AdIds(
    @SerializedName("interstitial_connection") var interstitialConnection: AdModel? = AdModel(),
    @SerializedName("interstitial_disconnect") var interstitialDisconnect: AdModel? = AdModel(),
    @SerializedName("interstitial_timer") var interstitialTimer: AdModel? = AdModel(),
    @SerializedName("interstitial_splash") var interstitialSplash: AdModel? = AdModel(),
    @SerializedName("banner_home") var bannerHome: AdModel? = AdModel(),
    @SerializedName("banner_servers") var bannerServers: AdModel? = AdModel(),
    @SerializedName("banner_connected") var bannerConnected: AdModel? = AdModel(),
    @SerializedName("native_exit") var nativeExit: AdModel? = AdModel(),
    @SerializedName("native_languages") var nativeLanguages: AdModel? = AdModel(),
    @SerializedName("native_home") var nativeHome: AdModel? = AdModel(),
    @SerializedName("app_open") var appOpen: AdModel? = AdModel(),
    @SerializedName("rewarded1_ad") var rewardedAd1: AdModel? = AdModel(),
    @SerializedName("rewarded2_ad") var rewardedAd2: AdModel? = AdModel(),
    @SerializedName("rewarded3_ad") var rewardedAd3: AdModel? = AdModel(),
    @SerializedName("free_trial_key") var freeTrialKey: AdModel? = AdModel(),
)