tasks.register("clean") {
    dependsOn(project.getTasksByName("clean", true).filter { it != this })
}
