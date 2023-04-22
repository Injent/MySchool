import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler

object Dependencies {
    const val ACCOMPANIST_SYSTEMUICONTROLLER = "com.google.accompanist:accompanist-systemuicontroller:${Versions.ACCOMPANIST}"
    const val ACCOMPANIST_PLACEHOLDER = "com.google.accompanist:accompanist-placeholder-material:${Versions.ACCOMPANIST}"
    const val ACCOMPANIST_PERMISSIONS = "com.google.accompanist:accompanist-permissions:${Versions.ACCOMPANIST}"
    const val ACTIVITY_COMPOSE = "androidx.activity:activity-compose:${Versions.AndroidX.ACTIVITY}"
    const val CORE_KTX = "androidx.core:core-ktx:${Versions.AndroidX.CORE}"
    const val CORE_SPLASHSCREEN = "androidx.core:core-splashscreen:${Versions.AndroidX.CORE_SPLASHSCREEN}"
    const val DATASTORE = "androidx.datastore:datastore:${Versions.AndroidX.DATA_STORE}"
    const val CONSTRAINT_LAYOUT_COMPOSE = "androidx.constraintlayout:constraintlayout-compose:${Versions.AndroidX.CONSTRAINT_LAYOUT}"
    const val COMPOSE_COMPILER = "androidx.compose.compiler:compiler:${Versions.AndroidX.COMPOSE_COMPILER}"
    const val COMPOSE_MATERIAL3 = "androidx.compose.material3:material3:${Versions.AndroidX.COMPOSE_MATERIAL3}"
    const val COMPOSE_UI = "androidx.compose.ui:ui:${Versions.AndroidX.COMPOSE_UI}"
    const val COMPOSE_UI_TOOLING_PREVIEW = "androidx.compose.ui:ui-tooling-preview:${Versions.AndroidX.COMPOSE_UI}"
    const val COMPOSE_UI_TOOLING = "androidx.compose.ui:ui-tooling:${Versions.AndroidX.COMPOSE_UI}"
    const val HILT_NAVIGATION_COMPOSE = "androidx.hilt:hilt-navigation-compose:${Versions.AndroidX.HILT}"
    const val LIFECYCLE_RUNTIME_COMPOSE = "androidx.lifecycle:lifecycle-runtime-compose:${Versions.AndroidX.LIFECYCLE_RUNTIME_COMPOSE}"
    const val LIFECYCLE_VIEWMODEL_COMPOSE = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.AndroidX.LIFECYCLE}"
    const val LIFECYCLE_RUNTIME_KTX = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.AndroidX.LIFECYCLE}"
    const val NAVIGATION_COMPOSE = "androidx.navigation:navigation-compose:${Versions.AndroidX.NAVIGATION}"
    const val ROOM_RUNTIME = "androidx.room:room-runtime:${Versions.AndroidX.ROOM}"
    const val ROOM_KTX = "androidx.room:room-ktx:${Versions.AndroidX.ROOM}"
    const val ROOM_COMPILER = "androidx.room:room-compiler:${Versions.AndroidX.ROOM}"
    const val HILT_ADNROID = "com.google.dagger:hilt-android:${Versions.HILT}"
    const val HILT_ANDROID_COMPILER = "com.google.dagger:hilt-android-compiler:${Versions.HILT}"
    const val HILT_COMPILER = "com.google.dagger:hilt-compiler:${Versions.HILT}"
    const val ANDROIDX_HILT_COMPILER = "androidx.hilt:hilt-compiler:${Versions.AndroidX.HILT}"
    const val HILT_GRADLE_PLUGIN = "com.google.dagger:hilt-android-gradle-plugin:${Versions.HILT}"
    const val DATETIME = "org.jetbrains.kotlinx:kotlinx-datetime:${Versions.KotlinX.DATETIME}"
    const val SERIALIZATION = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.KotlinX.SERIALIZATION}"
    const val COROUTINES = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.KotlinX.COROUTINES}"
    const val DESUGAR_JDK_LIBS = "com.android.tools:desugar_jdk_libs:2.0.2"
    const val KOTLIN = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}"
    const val KTOR_CORE = "io.ktor:ktor-client-core:${Versions.KTOR}"
    const val KTOR_CLIENT_ANDROID = "io.ktor:ktor-client-android:${Versions.KTOR}"
    const val KTOR_CONTENT_NEGOTATION = "io.ktor:ktor-client-content-negotiation:${Versions.KTOR}"
    const val KTOR_JSON_SERIALIZATION = "io.ktor:ktor-serialization-kotlinx-json:${Versions.KTOR}"
    const val RETROFIT = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}"
    const val PREFERENCES_DATA_STORE = "androidx.datastore:datastore-preferences:${Versions.AndroidX.DATA_STORE}"
    const val SECURITY_CRYPTO = "androidx.security:security-crypto:${Versions.AndroidX.SECURITY_CRYPTO}"
    const val RETROFIT_KOTLINX_SERIALIZATION_CONVERTER = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${Versions.RETROFIT_KOTLINX_SERIALIZATION}"
    const val OKHTTP = "com.squareup.okhttp3:okhttp:${Versions.OKHTTP}"
    const val STARTUP = "androidx.startup:startup-runtime:${Versions.AndroidX.STARTUP}"
    const val WORK = "androidx.work:work-runtime-ktx:${Versions.AndroidX.WORK}"
    const val HILT_WORK = "androidx.hilt:hilt-work:${Versions.AndroidX.HILT_WORK}"
    const val LIVEDATA = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.AndroidX.LIFECYCLE}"
    const val WINDOW_SIZE_CLASS = "androidx.compose.material3:material3-window-size-class:${Versions.AndroidX.WINDOW_SIZE_CLASS}"
    const val COIL = "io.coil-kt:coil-compose:${Versions.COIL}"
    const val FIREBASE_CRASHLYTICS_GRADLE = "com.google.firebase:firebase-crashlytics-gradle:2.9.5"
    const val FIREBASE_PERFOMANCE_GRADLE = "com.google.firebase:perf-plugin:1.4.2"
    const val FIREBASE_BOM = "com.google.firebase:firebase-bom:${Versions.Firebase.BOM}"
    const val FIREBASE_FIRESTORE = "com.google.firebase:firebase-firestore-ktx"
    const val FIREBASE_CRASHLYTICS = "com.google.firebase:firebase-crashlytics-ktx"
    const val FIREBASE_ANALYTICS = "com.google.firebase:firebase-analytics-ktx"
}

object Versions {
    const val TARGET_ANDROID_SDK = 33
    const val MIN_ANDROID_SDK = 23
    const val KOTLIN = "1.8.10"

    const val ACCOMPANIST = "0.29.2-rc"
    const val HILT = "2.44.2"
    const val KTOR = "2.2.3"
    const val RETROFIT = "2.9.0"
    const val RETROFIT_KOTLINX_SERIALIZATION = "0.8.0"
    const val OKHTTP = "4.10.0"
    const val COIL = "2.3.0"

    object Firebase {
        const val BOM = "31.5.0"
    }

    object AndroidX {
        const val CORE = "1.9.0"
        const val CORE_SPLASHSCREEN = "1.0.0-beta02"
        const val DATA_STORE = "1.0.0"
        const val LIFECYCLE = "2.5.1"
        const val LIFECYCLE_RUNTIME_COMPOSE = "2.6.0-beta01"
        const val ACTIVITY = "1.6.1"
        const val CONSTRAINT_LAYOUT = "1.0.1"

        const val COMPOSE_ANIMATION = "1.3.3"
        const val COMPOSE_COMPILER = "1.4.2"
        const val COMPOSE_FOUNDATION = "1.3.1"
        const val COMPOSE_MATERIAL3 = "1.1.0-alpha06"
        const val COMPOSE_RUNTIME = "1.3.3"
        const val COMPOSE_UI = "1.3.3"

        const val HILT = "1.0.0"
        const val NAVIGATION = "2.5.3"
        const val ROOM = "2.5.0"
        const val SECURITY_CRYPTO = "1.1.0-alpha01"
        const val WORK = "2.7.1"
        const val HILT_WORK = "1.0.0"
        const val STARTUP = "1.1.1"
        const val WINDOW_SIZE_CLASS = "1.0.1"
    }

    object KotlinX {
        const val COROUTINES = "1.3.9"
        const val DATETIME = "0.4.0"
        const val SERIALIZATION = "1.5.0"
    }
}

fun DependencyHandler.room() {
    implementation(Dependencies.ROOM_RUNTIME)
    implementation(Dependencies.ROOM_KTX)
    ksp(Dependencies.ROOM_COMPILER)
}

fun DependencyHandler.ktor() {
    implementation(Dependencies.KTOR_CORE)
    implementation(Dependencies.KTOR_CLIENT_ANDROID)
    implementation(Dependencies.KTOR_CONTENT_NEGOTATION)
    implementation(Dependencies.KTOR_JSON_SERIALIZATION)
}

fun DependencyHandler.hilt() {
    implementation(Dependencies.HILT_ADNROID)
    kapt(Dependencies.HILT_ANDROID_COMPILER)
    kapt(Dependencies.ANDROIDX_HILT_COMPILER)
    kapt(Dependencies.HILT_COMPILER)
}

fun DependencyHandler.compose() {
    implementation(Dependencies.COMPOSE_UI)
    implementation(Dependencies.COMPOSE_UI_TOOLING_PREVIEW)
    implementation(Dependencies.COMPOSE_MATERIAL3)
    implementation(Dependencies.CONSTRAINT_LAYOUT_COMPOSE)
    debugImplementation(Dependencies.COMPOSE_UI_TOOLING)
}

fun DependencyHandler.retrofit() {
    implementation(Dependencies.RETROFIT)
    implementation(Dependencies.RETROFIT_KOTLINX_SERIALIZATION_CONVERTER)
    implementation(Dependencies.OKHTTP)
}

internal fun DependencyHandler.debugImplementation(dependency: String) {
    add("debugImplementation", dependency)
}

internal fun DependencyHandler.implementation(dependency: String) {
    add("implementation", dependency)
}

internal fun DependencyHandler.implementation(dependency: Project) {
    add("implementation", dependency)
}

internal fun DependencyHandler.kapt(dependency: String) {
    add("kapt", dependency)
}

internal fun DependencyHandler.ksp(dependency: String) {
    add("ksp", dependency)
}