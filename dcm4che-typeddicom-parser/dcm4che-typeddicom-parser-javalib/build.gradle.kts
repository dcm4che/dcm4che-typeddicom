plugins {
    id("java-library")
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://www.dcm4che.org/maven2/")
    }
}

dependencies {
    implementation(project(":dcm4che-typeddicom-parser-dtos"))

    implementation("org.apache.commons:commons-text:1.9")
    implementation("com.github.davidmoten:word-wrap:0.1.9")

    implementation("org.apache.commons:commons-lang3:3.12.0")

    implementation("org.dcm4che:dcm4che-core:5.29.0")

    implementation("org.jsoup:jsoup:1.14.3")

    implementation("com.fasterxml.jackson.core:jackson-core:2.14.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")
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
