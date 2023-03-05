plugins {
    id("injent.android.library")
    id("injent.android.library.compose")
}

android {
    namespace = "me.injent.myschool.core.ui"
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:model"))
    implementation(Dependencies.COMPOSE_UI_TOOLING_PREVIEW)
    implementation(Dependencies.COMPOSE_UI_TOOLING)
}