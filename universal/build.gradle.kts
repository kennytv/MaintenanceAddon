plugins {
    id("maintenanceaddon.shadow-conventions")
}

val platforms = setOf(
    rootProject.projects.maintenanceaddonPaper,
    rootProject.projects.maintenanceaddonBungee,
    rootProject.projects.maintenanceaddonVelocity
).map { it.path }

tasks {
    shadowJar {
        manifest {
            attributes["paperweight-mappings-namespace"] = "mojang"
        }
        archiveFileName.set("MaintenanceAddon-${project.version}.jar")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        platforms.forEach { platformPath ->
            val platformProject = project(platformPath)
            val shadowJarTask = platformProject.tasks.shadowJar.get()
            dependsOn(shadowJarTask)
            dependsOn(platformProject.tasks.jar)
            from(zipTree(shadowJarTask.archiveFile))
        }
    }
}