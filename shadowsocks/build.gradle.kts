plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.github.shadowsocks"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        aidl = true
        buildConfig = true
    }
}

dependencies {

    val coroutinesVersion = "1.6.4"
    val roomVersion = "2.5.0"

    api("androidx.core:core-ktx:1.9.0")
    api("com.google.android.material:material:1.8.0")
    coreLibraryDesugaring ("com.android.tools:desugar_jdk_libs:2.0.2")
    api("androidx.lifecycle:lifecycle-livedata-core-ktx:2.8.7")
    api("androidx.preference:preference:1.2.0")
    api("androidx.room:room-runtime:$roomVersion")
    api("com.google.code.gson:gson:2.10.1")
    api("com.jakewharton.timber:timber:5.0.1")
    api("dnsjava:dnsjava:3.5.2")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$coroutinesVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    androidTestImplementation("androidx.room:room-testing:$roomVersion")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.5")
}