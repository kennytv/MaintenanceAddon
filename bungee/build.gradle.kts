plugins {
    id("maintenanceaddon.platform-conventions")
}

dependencies {
    implementation(projects.maintenanceaddonCommon)
    compileOnly(libs.bungee)
    compileOnly(libs.maintenance.bungee)
}