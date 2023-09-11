plugins {
    `java-library`
    id("org.dcm4che.typeddicom-java-generator")
    id("org.dcm4che.typeddicom-publisher")
}

generateTypeddicomJavaSources {
    privateDicomMetamodelYamlDirectory.set(java.sourceSets.getByName("main").resources.sourceDirectories.single())
    generatedJavaOutputDirectory.set(layout.buildDirectory.dir("typeddicom"))
}


typeddicomPublisher {
    pomName.set("dcm4che-typeddicom-lib-std")
    pomDescription.set("This library provides Java classes to wrap dcm4che Attributes and provide type safety for " +
            "standard DICOM tags.");
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://www.dcm4che.org/maven2/")
    }
}

dependencies {
    implementation("org.dcm4che:dcm4che-core:5.31.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.test {
    useJUnitPlatform()
}

// Remove non-numeric version info for Bundle version
val bundleVersion = "$version".replace(Regex(".*(\\d+\\.\\d+\\.\\d+).*"), "$1")

tasks.withType<Jar> {
    manifest {
        attributes["Manifest-Version"] = "1.0"
        attributes["Bundle-ManifestVersion"] = "2"
        attributes["Bundle-Name"] = "org.dcm4che.dcm4che-typeddicom-lib-std"
        attributes["Bundle-SymbolicName"] = "org.dcm4che.dcm4che-typeddicom-lib-std;singleton:=true"
        attributes["Bundle-Version"] = bundleVersion
        attributes["Bundle-Vendor"] = "dcm4che"
        attributes["Bundle-ClassPath"] = "."
        attributes["Bundle-ActivationPolicy"] = "lazy"
        attributes["Require-Bundle"] =
            "org.dcm4che.dcm4che-typeddicom-skeleton; bundle-version=\"${bundleVersion}\"; visibility:=reexport"
        attributes["Export-Package"] =
            "org.dcm4che.typeddicom, org.dcm4che.typeddicom.dataelements, org.dcm4che.typeddicom.iods, " +
                    "org.dcm4che.typeddicom.modules, org.dcm4che.typeddicom.valuerepresentations"
        attributes["Import-Package"] = "org.dcm4che3.data"
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}
