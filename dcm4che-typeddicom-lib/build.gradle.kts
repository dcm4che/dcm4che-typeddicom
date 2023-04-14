tasks.register("publishAllPublicationsToDcm4cheMavenRepository") {
    dependsOn(project.getTasksByName("publishAllPublicationsToDcm4cheMavenRepository", true).filter { it != this })
}

tasks.register("publishToMavenLocal") {
    dependsOn(project.getTasksByName("publishToMavenLocal", true).filter { it != this })
}

tasks.register("clean") {
    dependsOn(project.getTasksByName("clean", true).filter { it != this })
}
