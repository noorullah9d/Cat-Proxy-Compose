plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
}

android {
    namespace = "ai.galixo.proxy"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.free.vpn.unblock.proxy.hotspot.securevpn"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false

            resValue("string", "base_url", "https://proxydevapi.fusionsai.net/")
//            resValue("string", "base_url", "https://proxyapiv2.fusionsai.net/")
            resValue("string", "support_url", "https://api.9dtechnologies.dev/api/support/")

            // test ad ids
            resValue("string", "admob_app_id", "ca-app-pub-3940256099942544~3347511713")
            resValue("string", "admob_interstitial_id_connection", "ca-app-pub-3940256099942544/1033173712")
            resValue("string", "admob_interstitial_id_disconnect", "ca-app-pub-3940256099942544/1033173712")
            resValue("string", "admob_interstitial_id_timer", "ca-app-pub-3940256099942544/1033173712")
            resValue("string", "admob_interstitial_id_splash", "ca-app-pub-3940256099942544/1033173712")
            resValue("string", "admob_banner_id_home", "ca-app-pub-3940256099942544/6300978111")
            resValue("string", "admob_banner_id_servers", "ca-app-pub-3940256099942544/6300978111")
            resValue("string", "admob_banner_id_connected", "ca-app-pub-3940256099942544/6300978111")
            resValue("string", "admob_native_id_exit", "ca-app-pub-3940256099942544/2247696110")
            resValue("string", "admob_native_id_languages", "ca-app-pub-3940256099942544/2247696110")
            resValue("string", "admob_native_id_home", "ca-app-pub-3940256099942544/2247696110")
            resValue("string", "admob_app_open_id", "ca-app-pub-3940256099942544/9257395921")
            resValue("string", "admob_rewarded1_ad_id", "ca-app-pub-3940256099942544/5224354917")
            resValue("string", "admob_rewarded2_ad_id", "ca-app-pub-3940256099942544/5224354917")
            resValue("string", "admob_rewarded3_ad_id", "ca-app-pub-3940256099942544/5224354917")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = true

            resValue("string", "base_url", "https://proxyapiv2.fusionsai.net/")
            resValue("string", "support_url", "https://api.9dtechnologies.dev/api/support/")

            // original ad ids
            resValue("string", "admob_app_id", "ca-app-pub-7229352898267255~5595091895")
            resValue("string", "admob_interstitial_id_connection", "ca-app-pub-7229352898267255/1884979347")
            resValue("string", "admob_interstitial_id_disconnect", "ca-app-pub-7229352898267255/2277005165")
            resValue("string", "admob_interstitial_id_timer", "ca-app-pub-7229352898267255/7838030679")
            resValue("string", "admob_interstitial_id_splash", "ca-app-pub-7229352898267255/7698429875")
            resValue("string", "admob_banner_id_home", "ca-app-pub-7229352898267255/2203156420")
            resValue("string", "admob_banner_id_servers", "ca-app-pub-7229352898267255/1327891166")
            resValue("string", "admob_banner_id_connected", "ca-app-pub-7229352898267255/6632188739")
            resValue("string", "admob_native_id_exit", "ca-app-pub-7229352898267255/1787180533")
            resValue("string", "admob_native_id_languages", "ca-app-pub-7229352898267255/1797408530")
            resValue("string", "admob_native_id_home", "ca-app-pub-7229352898267255/7011587059")
            resValue("string", "admob_app_open_id", "ca-app-pub-7229352898267255/2397554626")
            resValue("string", "admob_rewarded1_ad_id", "ca-app-pub-7229352898267255/9385203918")
            resValue("string", "admob_rewarded2_ad_id", "ca-app-pub-7229352898267255/6639621072")
            resValue("string", "admob_rewarded3_ad_id", "ca-app-pub-7229352898267255/5445958901")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
        viewBinding = true
    }
    packaging {
        jniLibs.useLegacyPackaging = true
    }
    bundle {
        language {
            // Specifies that the app bundle should not support
            // configuration APKs for language resources. These
            // resources are instead packaged with each base and
            // dynamic feature APK.
            enableSplit = false
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // ump
    implementation(libs.user.messaging.platform)

    // ads
    implementation(libs.play.services.ads)

    // in-app purchase
    implementation(libs.billing.ktx)

    // firebase
    implementation(libs.firebase.crashlytics.ktx)
    implementation(libs.firebase.analytics.ktx)
    implementation(libs.firebase.config)
    implementation(libs.firebase.messaging.ktx)

    // Google Maps Compose library
    implementation(libs.maps.compose)

    // accompanist pager
    implementation(libs.accompanist.pager)

    //room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // shadow-socks
    implementation(project(":shadowsocks"))
    implementation(project(":cypherLib"))

    // hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // lottie compose
    implementation(libs.lottie.compose)

    // coil compose
    implementation(libs.coil.compose)

    // compose navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    // lifecycle
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.runtime.livedata)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    coreLibraryDesugaring (libs.desugar.jdk.libs)
}