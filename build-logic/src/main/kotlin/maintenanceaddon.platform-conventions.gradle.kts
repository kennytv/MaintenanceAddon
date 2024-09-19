plugins {
    id("maintenanceaddon.shadow-conventions")
}

tasks {
    shadowJar {
        archiveFileName.set(
            "MaintenanceAddon-${
                project.name.substringAfter("maintenanceaddon-").replaceFirstChar { it.titlecase() }
            }-${project.version}.jar"
        )
    }
}