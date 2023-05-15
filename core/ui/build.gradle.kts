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
    implementation(project(":core:common"))
    implementation(Dependencies.DATETIME)
    implementation(Dependencies.COMPOSE_UI_TOOLING_PREVIEW)
    implementation(Dependencies.COMPOSE_UI_TOOLING)
    implementation(Dependencies.LIFECYCLE_VIEWMODEL_SCOPE)
    implementation(Dependencies.LIFECYCLE_RUNTIME_COMPOSE)
}