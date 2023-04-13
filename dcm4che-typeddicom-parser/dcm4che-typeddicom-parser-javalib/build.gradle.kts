import java.util.*

val rootProperties = Properties()
File("./gradle.properties").inputStream().use {
    rootProperties.load(it)
}

group = rootProperties.getProperty("group")
version = rootProperties.getProperty("version")

plugins {
    id("java-library")
}

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri("http://maven.scijava.org/content/repositories/public/")
        isAllowInsecureProtocol = true
    }
}

dependencies {
    implementation("org.apache.commons:commons-text:1.9")
    implementation("com.github.davidmoten:word-wrap:0.1.9")

    implementation("org.apache.commons:commons-lang3:3.12.0")

    implementation("org.dcm4che:dcm4che-core:5.23.3")

    implementation("org.jsoup:jsoup:1.14.3")

    implementation("com.fasterxml.jackson.core:jackson-core:2.14.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.14.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.14.2")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}


tasks.test {
    useJUnitPlatform()
}
