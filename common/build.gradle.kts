plugins {
    id("maintenanceaddon.base-conventions")
    id("net.kyori.blossom")
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