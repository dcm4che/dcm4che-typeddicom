import org.gradle.internal.impldep.org.apache.ivy.core.IvyPatternHelper.substitute

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
include("dcm4che-typeddicom-parser-dtos")
include("dcm4che-typeddicom-parser-javalib")
include("dcm4che-typeddicom-parser-gradleplugin")
