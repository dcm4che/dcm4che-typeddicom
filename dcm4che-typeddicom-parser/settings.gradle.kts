pluginManagement {
    repositories {
        mavenLocal()
        maven {
            url = uri("https://www.dcm4che.org/maven2/")
        }
        gradlePluginPortal()
    }
}

rootProject.name = "dcm4che-typeddicom-parser"
include("dcm4che-typeddicom-parser-javalib")
include("dcm4che-typeddicom-parser-gradleplugin")
