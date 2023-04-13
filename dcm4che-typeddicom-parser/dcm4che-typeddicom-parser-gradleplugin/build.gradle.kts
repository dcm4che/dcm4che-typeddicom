import java.util.*

val rootProperties = Properties()
File("./gradle.properties").inputStream().use {
    rootProperties.load(it)
}

group = rootProperties.getProperty("group")
version = rootProperties.getProperty("version")

plugins {
    `java-gradle-plugin`
}

gradlePlugin {
    plugins {
        create("dicomXmlParser") {
            id = "org.dcm4che.typeddicom-xml-parser"
            implementationClass = "org.dcm4che.typeddicom.parser.gradleplugin.DicomXmlParserPlugin"
        }
    }
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
    implementation(project(":dcm4che-typeddicom-parser-javalib"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
}


tasks.test {
    useJUnitPlatform()
}
