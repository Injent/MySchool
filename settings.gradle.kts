@file:Suppress("UnstableApiUsage")
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "MySchool"
include(":app")
include(":core:designsystem")
include(":feature:authorization")
include(":core:database")
include(":sync")
include(":feature:profile")
include(":core:network")
include(":core:model")
include(":core:data")
include(":core:common")
include(":core:datastore")
include(":feature:students")
include(":core:ui")