plugins {
    id("java-library")
    id("org.dcm4che.typeddicom-publisher")
}

typeddicomPublisher {
    pomName.set("dcm4che-typeddicom-skeleton")
    pomDescription.set("This contains the skeleton implementation of the Java classes for typeddicom libraries. It " +
            "isn't very useful on it's own. (see dcm4che-typeddicom-lib-std for a full fledged implementation of the " +
            "DICOM standard.");
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://www.dcm4che.org/maven2/")
    }
}

dependencies {
    implementation("org.dcm4che:dcm4che-core:5.31.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

// Remove non-numeric version info for Bundle version
val bundleVersion = "$version".replace(Regex(".*(\\d+\\.\\d+\\.\\d+).*"), "$1")

tasks.withType<Jar> {
    manifest {
        attributes["Manifest-Version"] = "1.0"
        attributes["Bundle-ManifestVersion"] = "2"
        attributes["Bundle-Name"] = "org.dcm4che.dcm4che-typeddicom-skeleton"
        attributes["Bundle-SymbolicName"] = "org.dcm4che.dcm4che-typeddicom-skeleton;singleton:=true"
        attributes["Bundle-Version"] = bundleVersion
        attributes["Bundle-Vendor"] = "dcm4che"
        attributes["Bundle-ClassPath"] = "."
        attributes["Bundle-ActivationPolicy"] = "lazy"
        attributes["Export-Package"] =
            "org.dcm4che.typeddicom, org.dcm4che.typeddicom.valuerepresentations"
        attributes["Import-Package"] = "org.dcm4che3.data"
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
