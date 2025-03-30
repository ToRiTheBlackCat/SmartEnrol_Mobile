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
//dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
//    repositories {
//        google()
//        mavenCentral()
//    }
//}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        google()
        maven("https://maven.pkg.jetbrains.space/data2viz/p/maven/dev")
        maven("https://maven.pkg.jetbrains.space/data2viz/p/maven/public")
        maven(url = uri("https://jitpack.io"))
        mavenCentral()
    }
}


rootProject.name = "SmartEnroll1"
include(":app")
