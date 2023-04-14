pluginManagement {
    repositories {
        mavenLocal()
        maven {
            url = uri("https://www.dcm4che.org/maven2/")
        }
        gradlePluginPortal()
    }
}

rootProject.name = "dcm4che-typeddicom-generator"
includeBuild("../dcm4che-typeddicom-publisher")
includeBuild("../dcm4che-typeddicom-parser") {
    dependencySubstitution {
        substitute(module("org.dcm4che:dcm4che-typeddicom-parser-javalib")).using(project(":dcm4che-typeddicom-parser-javalib"))
    }
}
include("dcm4che-typeddicom-java-generator-lib")
include("dcm4che-typeddicom-java-generator-gradleplugin")
