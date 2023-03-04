import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("injent.android.library")
                apply("injent.android.hilt")
            }

            dependencies {
                implementation(project(":core:designsystem"))
                implementation(project(":core:data"))
                implementation(project(":core:model"))
                implementation(project(":core:common"))
                implementation(project(":sync"))
                implementation(Dependencies.NAVIGATION_COMPOSE)
                implementation(Dependencies.HILT_NAVIGATION_COMPOSE)
                implementation(Dependencies.LIFECYCLE_RUNTIME_COMPOSE)
            }
        }
    }
}
