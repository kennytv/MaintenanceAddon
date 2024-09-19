import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("maintenanceaddon.base-conventions")
    id("com.gradleup.shadow")
}

tasks {
    jar {
        archiveClassifier.set("unshaded")
    }
    shadowJar {
        archiveClassifier.set("")
        destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))
    }
    build {
        dependsOn(shadowJar)
    }
}