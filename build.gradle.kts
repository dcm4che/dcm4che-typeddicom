tasks.register("publishAllPublicationsToDcm4cheMavenRepository") {
    dependsOn(gradle.includedBuilds.map { it.task(":publishAllPublicationsToDcm4cheMavenRepository") })
}
