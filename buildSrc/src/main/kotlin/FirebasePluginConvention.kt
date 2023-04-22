import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class FirebasePluginConvention : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.gms.google-services")
                apply("com.google.firebase.firebase-perf")
                apply("com.google.firebase.crashlytics")
            }

            dependencies {
                add(
                    "implementation",
                    platform(Dependencies.FIREBASE_BOM)
                )
                implementation("com.google.firebase:firebase-perf-ktx")
                implementation("com.google.firebase:firebase-crashlytics-ktx")
                implementation("com.google.firebase:firebase-analytics-ktx")
                implementation("com.google.firebase:firebase-firestore-ktx")
                implementation("com.google.firebase:firebase-firestore-ktx")
            }
//
//            extensions.configure<ApplicationAndroidComponentsExtension> {
//                finalizeDsl {
//                    it.buildTypes.forEach { buildType ->
//                        // Disable the Crashlytics mapping file upload. This feature should only be
//                        // enabled if a Firebase backend is available and configured in
//                        // google-services.json.
//                        buildType.configure<CrashlyticsExtension> {
//                            mappingFileUploadEnabled = false
//                        }
//                    }
//                }
//            }
        }
    }
}