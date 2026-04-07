plugins {
    id("maintenanceaddon.platform-conventions")
}

tasks {
    shadowJar {
        relocate("net.kyori", "eu.kennytv.maintenance.addon.lib.kyori") {
            exclude("net.kyori", "adventure-bom")
            exclude("com.google.code.gson", "gson")
        }
    }
}

dependencies {
    implementation(projects.maintenanceaddonCommon)
    compileOnly(libs.bungee)
    compileOnly(libs.maintenance.core.proxy)

    api(libs.bundles.adventureBungee) {
        exclude("com.google.code.gson", "gson")
        exclude("org.slf4j", "slf4j-api")
        exclude("net.kyori", "adventure-bom")
    }
}