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
  id("kotlin-android-extensions")
  id("com.google.gms.google-services")
  id("com.google.firebase.crashlytics")
}

val versionMajor = 0
val versionMinor = 6
val versionPatch = 0
val isBeta = false

android {
  compileSdkVersion(29)
  defaultConfig {
    applicationId = "app.evergreen"
    minSdkVersion(23)
    targetSdkVersion(29)
    versionCode = versionMajor * 10000 + versionMinor * 100 + versionPatch
    versionName = "${versionMajor}.${versionMinor}.${versionPatch}" + if (isBeta) "-BETA" else ""
    vectorDrawables.useSupportLibrary = true
  }

  signingConfigs {
    register("appSigningKey") {
      val keystorePropsFile = File("../keys/keystore.properties")
      if (keystorePropsFile.exists()) {
        val props = Properties()
        props.load(FileInputStream(keystorePropsFile))

        storeFile = file(props.getProperty("APP_SIGNING_KEY_FILE"))
        storePassword = props.getProperty("APP_SIGNING_KEY_STORE_PASSWORD")
        keyAlias = props.getProperty("APP_SIGNING_KEY_ALIAS")
        keyPassword = props.getProperty("APP_SIGNING_KEY_KEY_PASSWORD")
      }
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
  implementation(project(":config"))

  // Kotlin.
  implementation(kotlin("stdlib", org.jetbrains.kotlin.config.KotlinCompilerVersion.VERSION))
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.3")
  implementation("androidx.core:core-ktx:1.2.0")

  // AndroidX
  implementation("androidx.constraintlayout:constraintlayout:1.1.3")
  implementation("androidx.leanback:leanback:1.0.0")
  implementation("androidx.work:work-runtime:2.3.1")

  // Third Party Libraries
  implementation("com.squareup.okhttp3:okhttp:4.2.2")
  implementation("io.coil-kt:coil:0.9.5")
  implementation("com.google.firebase:firebase-analytics:17.2.2")
  implementation("com.google.firebase:firebase-crashlytics:17.0.0-beta01")
}
