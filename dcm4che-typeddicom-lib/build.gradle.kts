val typeddicomVersion by extra { project.version }


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

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications["mavenJava"])
}

java.sourceSets["main"].java {
    srcDir("build/java")
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://www.dcm4che.org/maven2/")
    }
}

dependencies {
    implementation("org.dcm4che:dcm4che-core:5.25.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
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

tasks.named<Jar>("jar").configure {
    archiveFileName.set("${project.name}.jar")
}

tasks.named<Jar>("javadocJar").configure {
    archiveFileName.set("${project.name}-javadoc.jar")
}

tasks.named<Jar>("sourcesJar").configure {
    archiveFileName.set("${project.name}-sources.jar")
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
