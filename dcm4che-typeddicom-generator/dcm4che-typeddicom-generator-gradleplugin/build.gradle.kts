plugins {
    `java-gradle-plugin`
    id("org.dcm4che.typeddicom.parser.gradleplugin")
}

processDicomXml {
    dicomStandardXmlDirectory.set(layout.projectDirectory.dir("src/main/resources/dicom-standard-xml"))
    generatedYamlMetamodelOutputDirectory.set(layout.buildDirectory.dir("typeddicom-generated/resources"))
}

gradlePlugin {
    plugins {
        create("generateJavaSourceFiles") {
            id = "org.dcm4che.typeddicom.generator.gradleplugin"
            implementationClass = "org.dcm4che.typeddicom.generator.gradleplugin.GenerateTypeddicomJavaSourcesPlugin"
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
    sourceSets {
        main {
            resources {
                srcDir(layout.buildDirectory.dir("typeddicom-generated/resources"))
            }
        }
    }
}

tasks.named("processResources") {
    dependsOn(tasks.named("generateYamlFiles"))
}

tasks.withType<Jar> {
    exclude("dicom-standard-xml/")
}

tasks.test {
    useJUnitPlatform()
}
