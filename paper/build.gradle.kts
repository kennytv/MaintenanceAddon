plugins {
    id("maintenanceaddon.platform-conventions")
}

dependencies {
    implementation(projects.maintenanceaddonCommon)
    compileOnly(libs.maintenance.api)

    compileOnly(libs.paper)

    compileOnly(libs.miniplaceholders)
    compileOnly(libs.placeholderapi)
}