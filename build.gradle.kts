val releasableProjects = arrayOf(
    "dcm4che-typeddicom-parser",
    "dcm4che-typeddicom-generator",
    "dcm4che-typeddicom-lib",
    "dcm4che-typeddicom-skeleton"
)

tasks.register("publishAllPublicationsToDcm4cheMavenRepository") {
    dependsOn(gradle.includedBuilds
        .filter { it.name in releasableProjects }
        .map { it.task(":publishAllPublicationsToDcm4cheMavenRepository") }
    )
}

tasks.register("publishToMavenLocal") {
    dependsOn(gradle.includedBuilds
        .filter { it.name in releasableProjects }
        .map { it.task(":publishToMavenLocal") }
    )
}

tasks.register("clean") {
    dependsOn(gradle.includedBuilds
        .map { it.task(":clean") }
    )
}

tasks.register("build") {
    dependsOn(gradle.includedBuilds
        .map { it.task(":build") }
    )
}
