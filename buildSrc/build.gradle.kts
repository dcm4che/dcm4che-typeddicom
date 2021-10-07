plugins {
    `java-gradle-plugin`
}

gradlePlugin {
    plugins {
        create("processDicomXml") {
            id = "com.agfa.typeddicom.gradleplugin.sourcegeneration"
            implementationClass = "com.agfa.typeddicom.gradleplugin.ProcessDicomXmlPlugin"
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
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.5")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.12.5")

    implementation("com.github.spullara.mustache.java:compiler:0.9.10")

    implementation("org.apache.commons:commons-text:1.9")

    implementation("org.dcm4che:dcm4che-core:5.23.3")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
}


tasks.test {
    useJUnitPlatform()
}


