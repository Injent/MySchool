import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.kotlin

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
                implementation(Dependencies.NAVIGATION_COMPOSE)
                implementation(Dependencies.HILT_NAVIGATION_COMPOSE)
            }
        }
    }
}
