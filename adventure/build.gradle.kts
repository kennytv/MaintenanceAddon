plugins {
    id("maintenanceaddon.base-conventions")
    id("com.gradleup.shadow")
}

tasks {
    shadowJar {
        relocate("net.kyori", "eu.kennytv.maintenance.addon.lib.kyori")
    }
    build {
        dependsOn(shadowJar)
    }
}

dependencies {
    api(libs.bundles.adventure) {
        exclude("net.kyori", "adventure-api")
        exclude("net.kyori", "adventure-bom")
    }
}