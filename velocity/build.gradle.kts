plugins {
    id("maintenanceaddon.platform-conventions")
}

dependencies {
    implementation(projects.maintenanceaddonCommon)
    compileOnly(libs.maintenance.core.proxy)

    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)

    compileOnly(libs.miniplaceholders)
}