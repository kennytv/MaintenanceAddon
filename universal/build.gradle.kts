plugins {
    id("maintenanceaddon.shadow-conventions")
}

val platforms = setOf(
    rootProject.projects.maintenanceaddonPaper,
    rootProject.projects.maintenanceaddonBungee,
    rootProject.projects.maintenanceaddonVelocity
).map { it.dependencyProject }

tasks {
    shadowJar {
        manifest {
            attributes["paperweight-mappings-namespace"] = "mojang"
        }
        archiveFileName.set("MaintenanceAddon-${project.version}.jar")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        platforms.forEach { platform ->
            val shadowJarTask = platform.tasks.shadowJar.get()
            dependsOn(shadowJarTask)
            dependsOn(platform.tasks.jar)
            from(zipTree(shadowJarTask.archiveFile))
        }
    }
}