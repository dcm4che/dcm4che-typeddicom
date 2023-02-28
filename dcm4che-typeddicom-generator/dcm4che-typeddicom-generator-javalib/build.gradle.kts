val typeddicomVersion: String by extra { project.version as String }

plugins {
    id("java-library")
}

group = "org.dcm4che"
version = "0.4.4-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri("http://maven.scijava.org/content/repositories/public/")
        isAllowInsecureProtocol = true
    }
}

dependencies {
    implementation("org.dcm4che:dcm4che-typeddicom-parser-javalib:$typeddicomVersion")

    implementation("com.github.spullara.mustache.java:compiler:0.9.10")

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
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
}


tasks.test {
    useJUnitPlatform()
}
