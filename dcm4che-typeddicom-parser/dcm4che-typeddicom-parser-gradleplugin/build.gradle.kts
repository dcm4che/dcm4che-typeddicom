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
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}


tasks.test {
    useJUnitPlatform()
}
