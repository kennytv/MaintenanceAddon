plugins {
    `java-library`
}

group = rootProject.group
version = rootProject.version
description = rootProject.description

tasks {
    // Variable replacements
    processResources {
        filesMatching(listOf("plugin.yml", "bungee.yml")) {
            expand("version" to project.version, "description" to project.description)
        }
    }
    compileJava {
        options.encoding = Charsets.UTF_8.name()
    }
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))