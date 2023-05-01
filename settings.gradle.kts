@file:Suppress("UnstableApiUsage")

include(":updates")


include(":core:navigation")


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

include(":core:analytics")
include(":core:auth")
include(":core:common")
include(":core:data")
include(":core:database")
include(":core:datastore")
include(":core:designsystem")
include(":core:domain")
include(":core:model")
include(":core:network")
include(":core:ui")

include(":feature:accounts")
include(":feature:auth")
include(":feature:dashboard")
include(":feature:diary")
include(":feature:homeworkdialog")
include(":feature:leaderboard")
include(":feature:markdetails")
include(":feature:myclass")
include(":feature:personmarks")
include(":feature:profile")
include(":feature:statistics")
include(":feature:userprofile")

include(":sync")