val releasableProjects = arrayOf(
    "dcm4che-typeddicom-generator",
    "dcm4che-typeddicom-lib"
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
