pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "iOSWeatherAppClone"
include(":app")
include(":core:model")
include(":core:network")
include(":core:database")
include(":core:datastore")
include(":core:location")
include(":core:navigation")
include(":core:designsystem")
include(":core:testing")
include(":feature:weather")
include(":feature:locations")
include(":feature:maps")
include(":feature:alerts")
include(":feature:settings")
include(":feature:widgets")
 
