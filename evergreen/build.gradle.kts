// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileInputStream
import java.util.*

plugins {
  id("com.android.application")
  id("kotlin-android")
  kotlin("kapt")
  id("com.github.triplet.play") version "3.4.0"
}

val versionMajor = 0
val versionMinor = 11
val versionPatch = 5

android {
  compileSdkVersion(29)
  defaultConfig {
    applicationId = "app.evergreen"
    minSdkVersion(23)
    targetSdkVersion(29)
    versionCode = versionMajor * 10000 + versionMinor * 100 + versionPatch
    versionName = "${versionMajor}.${versionMinor}.${versionPatch}"
    vectorDrawables.useSupportLibrary = true
  }

  signingConfigs {
    register("appSigningKey") {
      isV2SigningEnabled = true
      storeFile = file("../signing-keys.keystore")
      storePassword = System.getenv("KEYSTORE_PASSWORD")
      keyAlias = "evergreen"
      keyPassword = System.getenv("KEYSTORE_PASSWORD")
    }
  }

  buildTypes {
    named("release") {
      isMinifyEnabled = true
      isShrinkResources = true
      isCrunchPngs = false
      isZipAlignEnabled = true
      ext["alwaysUpdateBuildId"] = false // Crashlytics.
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "evergreen.pro", "moshi.pro", "kotlin.pro", "moshi-kotlin.pro"
      )
      resValue("string", "app_version", "${defaultConfig.versionName}")
      signingConfig = signingConfigs.getByName("appSigningKey")
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
}

tasks.withType<KotlinCompile>().configureEach {
  kotlinOptions {
    allWarningsAsErrors = true
    jvmTarget = "1.8"
  }
}

dependencies {
  // Kotlin.
  implementation(kotlin("stdlib", org.jetbrains.kotlin.config.KotlinCompilerVersion.VERSION))
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0")

  // AndroidX
  implementation("androidx.core:core-ktx:1.5.0")
  implementation("androidx.constraintlayout:constraintlayout:2.0.4")
  implementation("androidx.leanback:leanback:1.0.0")
  implementation("androidx.work:work-runtime:2.7.0-alpha03")

  // Moshi
  api("com.squareup.moshi:moshi:1.12.0")
  api("com.squareup.moshi:moshi-adapters:1.10.0")
  kapt("com.squareup.moshi:moshi-kotlin-codegen:1.12.0")

  // Third Party Libraries
  implementation("com.squareup.okhttp3:okhttp:4.9.1")
  implementation("io.coil-kt:coil:1.2.1")
}

play {
  serviceAccountCredentials.set(file("../service-account-keys.json"))

  // GitHub Actions automatically pushes to Alpha, so that’s our starting point.
  track.set("alpha")
  fromTrack.set("alpha")

  // By default, promote to Beta (without requiring any additional arguments on the command line.)
  promoteTrack.set("beta")
}
