import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler

object Dependencies {
    const val ACCOMPANIST_SYSTEMUICONTROLLER = "com.google.accompanist:accompanist-systemuicontroller:${Versions.ACCOMPANIST_SYSTEM_UI_CONTROLLER}"
    const val ACCOMPANIST_PLACEHOLDER = "com.google.accompanist:accompanist-placeholder-material:${Versions.ACCOMPANIST_PLACEHOLDER}"
    const val ACCOMPANIST_PERMISSIONS = "com.google.accompanist:accompanist-permissions:${Versions.ACCOMPANIST_PERMISSIONS}"
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
    const val KSP = ""
    const val WINDOW_SIZE_CLASS = "androidx.compose.material3:material3-window-size-class:${Versions.AndroidX.WINDOW_SIZE_CLASS}"
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
}

fun DependencyHandler.retrofit() {
    implementation(Dependencies.RETROFIT)
    implementation(Dependencies.RETROFIT_KOTLINX_SERIALIZATION_CONVERTER)
    implementation(Dependencies.OKHTTP)
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