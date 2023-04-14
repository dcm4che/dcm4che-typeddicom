import java.util.*

val rootProperties = Properties()
File("./gradle.properties").inputStream().use {
    rootProperties.load(it)
}

group = rootProperties.getProperty("group")
version = rootProperties.getProperty("version")

plugins {
    `java-gradle-plugin`
    id("org.dcm4che.typeddicom-xml-parser")
    id("org.dcm4che.typeddicom-publisher")
}

processDicomXml {
    dicomStandardXmlDirectory.set(layout.projectDirectory.dir("src/main/resources/dicom-standard-xml"))
    generatedYamlMetamodelOutputDirectory.set(layout.buildDirectory.dir("typeddicom-generated/resources"))
}

gradlePlugin {
    plugins {
        create("typeddicomJavaGenerator") {
            id = "org.dcm4che.typeddicom-java-generator"
            implementationClass = "org.dcm4che.typeddicom.generator.gradleplugin.TypeddicomJavaGeneratorPlugin"
        }
    }
}

typeddicomPublisher {
    pomName.set("dcm4che-typeddicom-java-generator-gradleplugin")
    pomDescription.set("This gradle plugin provides a Task to generate Java code which provides the classes which " +
            "are specified in the DICOM standard. The classes can be extended with custom tags by providing a yaml " +
            "file containing the changes.");
}

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri("http://maven.scijava.org/content/repositories/public/")
        isAllowInsecureProtocol = true
    }
    maven {
        url = uri("https://www.dcm4che.org/maven2/")
    }
}

dependencies {
    implementation(project(":dcm4che-typeddicom-java-generator-lib"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
    withJavadocJar()
    withSourcesJar()
}

tasks.withType<Jar> {
    exclude("dicom-standard-xml/")
}

tasks.test {
    useJUnitPlatform()
}
