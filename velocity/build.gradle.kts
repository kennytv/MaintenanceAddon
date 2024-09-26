plugins {
    id("maintenanceaddon.platform-conventions")
}

dependencies {
    implementation(projects.maintenanceaddonCommon)
    compileOnly(libs.maintenance.velocity)

    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)

    compileOnly(libs.miniplaceholders)
}