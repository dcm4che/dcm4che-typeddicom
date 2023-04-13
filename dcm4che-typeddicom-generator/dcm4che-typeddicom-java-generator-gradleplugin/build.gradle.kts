import java.util.*

val rootProperties = Properties()
File("./gradle.properties").inputStream().use {
    rootProperties.load(it)
}

group = rootProperties.getProperty("group")
version = rootProperties.getProperty("version")

plugins {
    `java-gradle-plugin`
    `maven-publish`
    signing
    id("org.dcm4che.typeddicom-xml-parser")
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

publishing {
    publications {
        create<MavenPublication>("pluginMaven") {
            artifactId = project.name

            pom {
                name.set("dcm4che-typeddicom-java-generator-gradleplugin")
                description.set(
                    "This gradle plugin provides a Task to generate Java code which provides the classes which are " +
                            "specified in the DICOM standard. The classes can be extended with custom tags by " +
                            "providing a yaml file containing the changes."
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
