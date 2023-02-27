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
include("dcm4che-typeddicom-generator-javalib")
include("dcm4che-typeddicom-generator-gradleplugin")
