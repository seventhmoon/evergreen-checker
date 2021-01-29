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

plugins {
  id("java")
  kotlin("jvm")
  kotlin("kapt")
}

tasks.withType<KotlinCompile>().configureEach {
  kotlinOptions {
    allWarningsAsErrors = true
    jvmTarget = "1.8"
  }
}

dependencies {
  // Kotlin
  implementation(kotlin("stdlib", org.jetbrains.kotlin.config.KotlinCompilerVersion.VERSION))
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.0")

  // Moshi
  api("com.squareup.moshi:moshi:1.11.0")
  api("com.squareup.moshi:moshi-adapters:1.10.0")
  kapt("com.squareup.moshi:moshi-kotlin-codegen:1.11.0")
}
