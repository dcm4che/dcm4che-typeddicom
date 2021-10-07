plugins {
    `java-library`
    id("com.agfa.typeddicom.gradleplugin.sourcegeneration")
}

processDicomXml {
    dicomStandardXmlDirectory.set(layout.projectDirectory.dir("src/main/resources/dicom-standard-xml"))
    mustacheTemplateDirectory.set(layout.projectDirectory.dir("src/main/resources/templates"))
    generatedJavaOutputDirectory.set(layout.buildDirectory.dir("java"))
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
    exclude("dicom-standard-xml/")
    exclude("templates/")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>{
    options.encoding = "UTF-8"
}
