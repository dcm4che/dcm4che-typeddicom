val typeddicomVersion by extra { "0.1.0" }


plugins {
    `java-library`
    `maven-publish`
    signing
    id("org.dcm4che.typeddicom.gradleplugin.sourcegeneration")
}

processDicomXml {
    dicomStandardXmlDirectory.set(layout.projectDirectory.dir("src/main/resources/dicom-standard-xml"))
    mustacheTemplateDirectory.set(layout.projectDirectory.dir("src/main/resources/templates"))
    generatedJavaOutputDirectory.set(layout.buildDirectory.dir("java"))
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = rootProject.group.toString()
            artifactId = rootProject.name
            version = rootProject.version.toString()
            
            from(components["java"])

            pom {
                name.set("dcm4che-typeddicom")
                description.set("This library provides Java classes to wrap dcm4che Attributes and provide type safety for standard DICOM tags.")
                url.set("https://github.com/dcm4che/dcm4che-typeddicom")
                properties.set(mapOf(
                    "project.build.sourceEncoding" to "UTF-8"
                ))
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
         *  ORG_GRADLE_PROJECT_signing.keyId=GPG_KEY_ID
         *  ORG_GRADLE_PROJECT_signing.password=GPG_KEY_PASSWORD
         *  ORG_GRADLE_PROJECT_signing.secretKeyRingFile=GPG_KEYRING_FILE`
        */
        maven { 
            name = "dcm4cheMaven"
            url = uri("sftp://dcm4che.org:22/home/maven2")
            credentials(PasswordCredentials::class)
        }
    }
}


java.sourceSets["main"].java {
    srcDir("build/java")
}

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri("http://maven.scijava.org/content/repositories/public/")
        isAllowInsecureProtocol = true
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}

dependencies {
    implementation("org.dcm4che:dcm4che-core:5.23.3")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withJavadocJar()
    withSourcesJar()
}

tasks.test {
    useJUnitPlatform()
}

tasks.compileJava {
    dependsOn(tasks.generateJavaSourceFiles)
}

tasks.withType<Jar> {
    dependsOn(tasks.generateJavaSourceFiles)
    exclude("dicom-standard-xml/")
    exclude("templates/")
    manifest {
        attributes["Manifest-Version"] = "1.0"
        attributes["Bundle-ManifestVersion"] = "2"
        attributes["Bundle-Name"] = "org.dcm4che.typeddicom"
        attributes["Bundle-SymbolicName"] = "org.dcm4che.typeddicom;singleton:=true"
        attributes["Bundle-Version"] = typeddicomVersion
        attributes["Bundle-Vendor"] = "AGFA"
        attributes["Bundle-ClassPath"] = "."
        attributes["Bundle-ActivationPolicy"] = "lazy"
        attributes["Export-Package"] =
            "org.dcm4che.typeddicom, org.dcm4che.typeddicom.dataelements, org.dcm4che.typeddicom.iods, org.dcm4che.typeddicom.modules, org.dcm4che.typeddicom.valuerepresentations"
        attributes["Import-Package"] = "org.dcm4che3.data"
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}
