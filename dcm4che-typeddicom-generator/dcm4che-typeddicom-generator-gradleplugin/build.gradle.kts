plugins {
    `java-gradle-plugin`
}

gradlePlugin {
    plugins {
        create("processDicomXml") {
            id = "org.dcm4che.typeddicom.gradleplugin"
            implementationClass = "org.dcm4che.typeddicom.generator.gradleplugin.ProcessDicomXmlPlugin"
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
    implementation(project(":dcm4che-typeddicom-generator-javalib"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
}


tasks.test {
    useJUnitPlatform()
}
