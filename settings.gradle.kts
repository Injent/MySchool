@file:Suppress("UnstableApiUsage")

include(":feature:homeworkdialog")


include(":feature:dashboard")

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
include(":core:ui")
include(":core:database")
include(":core:network")
include(":core:model")
include(":core:data")
include(":core:common")
include(":core:datastore")

include(":feature:myclass")
include(":feature:leaderboard")
include(":feature:personmarks")
include(":feature:authorization")
include(":feature:usersearch")

include(":sync")