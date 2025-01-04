plugins {
    id("java")
    checkstyle
}

group = "io.github.timtaran.modelengine"
version = "0.0.1-DEV"

checkstyle {
    toolVersion = "10.18.1"
    maxWarnings = 0
}

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        name = "codemc"
        url = uri("https://repo.codemc.org/repository/maven-public/" )
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("dev.jorel:commandapi-bukkit-core:9.7.0")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

configurations.checkstyle {
    resolutionStrategy.capabilitiesResolution.withCapability("com.google.collections:google-collections") {
        select("com.google.guava:guava:23.0")
    }
}

tasks.processResources {
    filesMatching("**/paper-plugin.yml") {
        expand(
            "VERSION" to version,
            "NAME" to "Plugin",
            "GROUP" to project.group
        )
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}