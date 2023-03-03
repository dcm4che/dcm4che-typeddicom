pluginManagement {
    repositories {
        mavenLocal()
        maven {
            url = uri("https://www.dcm4che.org/maven2/")
        }
        gradlePluginPortal()
    }
}

rootProject.name = "dcm4che-typeddicom-lib"
include("dcm4che-typeddicom-lib-std")
includeBuild("../dcm4che-typeddicom-generator")
includeBuild("../dcm4che-typeddicom-skeleton") {
    dependencySubstitution {
        substitute(module("org.dcm4che:dcm4che-typeddicom-skeleton")).using(project(":"))
    }
}
