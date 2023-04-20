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
    mavenCentral()
    maven {
        url = uri("https://www.dcm4che.org/maven2/")
    }
}

dependencies {
    implementation(project(":dcm4che-typeddicom-parser-javalib"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}


tasks.test {
    useJUnitPlatform()
}
