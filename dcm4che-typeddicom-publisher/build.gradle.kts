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
        create("typeddicomPublisher") {
            id = "org.dcm4che.typeddicom-publisher"
            implementationClass = "org.dcm4che.typeddicom.publisher.TypeddicomPublisherPlugin"
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
