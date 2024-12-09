plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    alias(libs.plugins.compose.compiler)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.v2ray.ang"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.v2ray.ang"
        minSdk = 24
        targetSdk = 34
        versionCode = 586
        versionName = "1.8.40"
        multiDexEnabled = true
        splits {
            abi {
                isEnable = true
                include(
                    "arm64-v8a",
                    "armeabi-v7a",
                    "x86_64",
                    "x86"
                )
                isUniversalApk = true
            }
        }

    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildTypes {
        debug {
            isMinifyEnabled = false

            resValue("string", "base_url", "https://proxyapiv2.fusionsai.net/")
            resValue("string", "support_url", "https://api.9dtechnologies.dev/api/support/")
        }
        release {
            isMinifyEnabled = false

            resValue("string", "base_url", "https://proxyapiv2.fusionsai.net/")
            resValue("string", "support_url", "https://api.9dtechnologies.dev/api/support/")
        }
    }

    sourceSets {
        getByName("main") {
            jniLibs.srcDirs("libs")
        }
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    applicationVariants.all {
        val variant = this
        val versionCodes =
            mapOf("armeabi-v7a" to 4, "arm64-v8a" to 4, "x86" to 4, "x86_64" to 4, "universal" to 4)

        variant.outputs
            .map { it as com.android.build.gradle.internal.api.ApkVariantOutputImpl }
            .forEach { output ->
                val abi = if (output.getFilter("ABI") != null)
                    output.getFilter("ABI")
                else
                    "universal"

                output.outputFileName = "v2rayNG_${variant.versionName}_${abi}.apk"
                if (versionCodes.containsKey(abi)) {
                    output.versionCodeOverride = (1000000 * versionCodes[abi]!!).plus(variant.versionCode)
                } else {
                    return@forEach
                }
            }
    }

    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.6.0" // Same as your Compose version
    }

    packaging {
        jniLibs {
            useLegacyPackaging = true
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.aar", "*.jar"))))
    testImplementation(libs.junit)

    implementation(project(":cypherLib"))

    implementation(libs.androidx.core.ktx)

    // retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    //room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)

    // ------------------ COMPOSE START ----------------
    // Jetpack Compose Core
    implementation(libs.ui)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.ui.tooling)

    // Hilt dependencies
    implementation(libs.hilt.android)  // Hilt Android
    kapt(libs.hilt.compiler)

    // coil
    implementation(libs.coil.compose)

    // Compose Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    // Lifecycle Integration
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)

    // ViewModel Integration
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // ------------------ COMPOSE END ----------------

    implementation(libs.flexbox)

    // Androidx
    implementation(libs.constraintlayout)
    implementation(libs.legacy.support.v4)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.cardview)
    implementation(libs.preference.ktx)
    implementation(libs.recyclerview)
    implementation(libs.fragment.ktx)
    implementation(libs.multidex)
    implementation(libs.viewpager2)

    // Androidx ktx
    implementation(libs.activity.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.runtime.ktx)

    //kotlin
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.mmkv.static)
    implementation(libs.gson)
    implementation(libs.rxjava)
    implementation(libs.rxandroid)
    implementation(libs.rxpermissions)
    implementation(libs.toastcompat)
    implementation(libs.editorkit)
    implementation(libs.language.base)
    implementation(libs.language.json)
    implementation(libs.quickie.bundled)
    implementation(libs.core)
    implementation(libs.work.runtime.ktx)
    implementation(libs.work.multiprocess)
}