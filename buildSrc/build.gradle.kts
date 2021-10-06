plugins {
    `kotlin-dsl`
    id("io.freefair.lombok") version "6.2.0"
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
    implementation("org.codehaus.groovy:groovy-all:3.0.7")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.5")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.12.5")

    implementation("com.github.spullara.mustache.java:compiler:0.9.10")

    implementation("org.apache.commons:commons-text:1.9")

    implementation("org.dcm4che:dcm4che-core:5.23.3")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
}

tasks.test {
    useJUnitPlatform()
}


