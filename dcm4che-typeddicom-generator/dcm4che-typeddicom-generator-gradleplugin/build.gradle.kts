plugins {
    `java-gradle-plugin`
    id("org.dcm4che.typeddicom.parser.gradleplugin")
}

processDicomXml {
    dicomStandardXmlDirectory.set(layout.projectDirectory.dir("src/main/resources/dicom-standard-xml"))
    mustacheTemplateDirectory.set(layout.projectDirectory.dir("src/main/resources/templates"))
    generatedYamlMetamodelOutputDirectory.set(layout.buildDirectory.dir("typeddicom/metamodel"))
}

gradlePlugin {
    plugins {
        create("generateJavaSourceFiles") {
            id = "org.dcm4che.typeddicom.generator.gradleplugin"
            implementationClass = "org.dcm4che.typeddicom.generator.gradleplugin.GenerateTypeddicomJavaSourcesPlugin"
        }
    }
}

sourceSets {
    create("metamodel") {
        java.srcDir(layout.buildDirectory.dir("typeddicom/metamodel"))
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
