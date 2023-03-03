plugins {
    id("java-library")
}

group = "org.dcm4che"
version = "unspecified"

repositories {
    mavenCentral()
    maven {
        url = uri("https://www.dcm4che.org/maven2/")
    }
}

dependencies {
    implementation("org.dcm4che:dcm4che-core:5.25.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
