plugins {
    id("maintenanceaddon.platform-conventions")
}

dependencies {
    implementation(projects.maintenanceaddonCommon)

    compileOnly(libs.paper)
    compileOnly(libs.placeholderapi)
    compileOnly(libs.maintenance.api)
}