import java.util.*

val rootProperties = Properties()
File("./gradle.properties").inputStream().use {
    rootProperties.load(it)
}

group = rootProperties.getProperty("group")
version = rootProperties.getProperty("version")

plugins {
    `java-library`
    `maven-publish`
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
    implementation("org.dcm4che:dcm4che-typeddicom-parser-javalib:$version")

    implementation("com.github.spullara.mustache.java:compiler:0.9.10")

    implementation("org.apache.commons:commons-text:1.9")
    implementation("com.github.davidmoten:word-wrap:0.1.9")

    implementation("org.apache.commons:commons-lang3:3.12.0")

    implementation("org.dcm4che:dcm4che-core:5.23.3")

    implementation("org.jsoup:jsoup:1.14.3")

    implementation("com.fasterxml.jackson.core:jackson-core:2.14.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.14.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.14.2")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
    withJavadocJar()
    withSourcesJar()
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = project.name

            pom {
                name.set("dcm4che-typeddicom-java-generator-lib")
                description.set(
                    "This library provides classes and methods to generate java code from a typeddicom yaml file. It " +
                            "is used in the dcm4che-typeddicom-java-generator-gradleplugin but could also be used " +
                            "on its own."
                )
                url.set("https://github.com/dcm4che/dcm4che-typeddicom")
                properties.set(
                    mapOf(
                        "project.build.sourceEncoding" to "UTF-8"
                    )
                )
                licenses {
                    name.set("Mozilla Public License Version 2.0")
                    url.set("https://www.mozilla.org/en-US/MPL/2.0/")
                }
                developers {
                    developer {
                        id.set("Nirostar")
                        name.set("Niklas Roth")
                        email.set("36939232+nirostar@users.noreply.github.com")
                    }
                }
                scm {
                    url.set("https://github.com/dcm4che/dcm4che-typeddicom")
                    connection.set("scm:git:git://github.com:dcm4che/dcm4che-typeddicom.git")
                    developerConnection.set("scm:git:git@github.com:dcm4che/dcm4che-typeddicom.git")
                }
                issueManagement {
                    url.set("https://github.com/dcm4che/dcm4che-typeddicom/issues")
                    system.set("GitHub Issues")
                }
            }
        }
    }
    repositories {
        /**
         *  use `./gradlew publish -Pdcm4cheMavenUsername=DCM4CHE_USERNAME -Pdcm4cheMavenPassword=DCM4CHE_PASSWORD -Psigning.keyId=GPG_KEY_ID -Psigning.password=GPG_KEY_PASSWORD -Psigning.secretKeyRingFile=GPG_KEYRING_FILE` to publish
         *  or set the corresponding env variables before running `./gradlew publish`:
         *  ORG_GRADLE_PROJECT_dcm4cheMavenUsername=DCM4CHE_MAVEN_SSH_USERNAME
         *  ORG_GRADLE_PROJECT_dcm4cheMavenPassword=DCM4CHE_MAVEN_SSH_PASSWORD
         *  ORG_GRADLE_PROJECT_signingKey=GPG_PRIVATE_KEY
         *  ORG_GRADLE_PROJECT_signingPassword=GPG_KEY_PASSWORD
         */
        maven {
            name = "dcm4cheMaven"
            url = uri("sftp://dcm4che.org:22/home/maven2")
            credentials(PasswordCredentials::class)
        }
    }
}

// signing {
//     val signingKey: String? by project
//     val signingPassword: String? by project
//     useInMemoryPgpKeys(signingKey, signingPassword)
//     sign(publishing.publications["pluginMaven"])
// }
