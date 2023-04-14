plugins {
    id("java")
    id("org.dcm4che.typeddicom-publisher")
}

typeddicomPublisher {
    pomName.set("dcm4che-typeddicom-parser-dtos")
    pomDescription.set("This library provides the Java dtos needed to parse the YAML file generated by the " +
            "dcm4che-typeddicom-parser-gradleplugin");
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.14.2")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
