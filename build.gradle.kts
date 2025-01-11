plugins {
    id("java")
    id("io.freefair.lombok") version "8.11"
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
    implementation("org.spongepowered:configurate-yaml:4.0.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.1")
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("dev.jorel:commandapi-bukkit-core:9.7.0")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
    }
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

tasks.jar {
    val customJarDir: String? = System.getenv("CUSTOM_JAR_DIR")  // for server testing
    destinationDirectory.set(if (customJarDir != null) {
        file(customJarDir)
    } else {
        file("build/libs") // Default build directory
    })
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}