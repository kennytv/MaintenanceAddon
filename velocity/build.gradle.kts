plugins {
    id("maintenanceaddon.platform-conventions")
    id("net.kyori.blossom")
}

dependencies {
    implementation(projects.maintenanceaddonCommon)

    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)

    compileOnly(libs.maintenance.velocity)
}

sourceSets {
    main {
        blossom {
            javaSources {
                property("version", project.version.toString())
            }
        }
    }
}