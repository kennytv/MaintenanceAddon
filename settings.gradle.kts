enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "maintenanceaddon"

dependencyResolutionManagement {
    repositories {
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
        maven("https://s01.oss.sonatype.org/content/repositories/releases/")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        mavenCentral()
    }
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
}

pluginManagement {
    plugins {
        id("net.kyori.blossom") version "2.1.0"
        id("com.gradleup.shadow") version "8.3.5"
    }
}

includeBuild("build-logic")

include("adventure")

listOf(
    "common",
    "bungee",
    "paper",
    "velocity",
    "universal"
).forEach { subproject(it) }

fun subproject(name: String) {
    setupSubproject("maintenanceaddon-$name") {
        projectDir = file(name)
    }
}

inline fun setupSubproject(name: String, block: ProjectDescriptor.() -> Unit) {
    include(name)
    project(":$name").apply(block)
}