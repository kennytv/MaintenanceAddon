plugins {
    id("maintenanceaddon.platform-conventions")
}

dependencies {
    implementation(projects.maintenanceaddonCommon)
    implementation(projects.adventure) {
        targetConfiguration = "shadow"
    }
    compileOnly(libs.maintenance.api)

    compileOnly(libs.paper)

    compileOnly(libs.miniplaceholders)
    compileOnly(libs.placeholderapi)
}