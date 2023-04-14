package org.dcm4che.typeddicom.generator.gradleplugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskProvider;

public class TypeddicomJavaGeneratorPlugin implements Plugin<Project> {
    public void apply(final Project target) {
        final TypeddicomJavaGeneratorPluginExtension extension =
                target.getExtensions().create("generateTypeddicomJavaSources", TypeddicomJavaGeneratorPluginExtension.class);
        SourceSetContainer sourceSets = (SourceSetContainer) target.getProperties().get("sourceSets");
        sourceSets.named("main", sourceSet -> extension.getPrivateDicomMetamodelYamlDirectory().convention(sourceSet.getResources().getDestinationDirectory()));
        TaskProvider<GenerateJavaSourcesTask> generateJavaSourcesFileTaskProvider =
                target.getTasks().register("generateJavaSourceFiles", GenerateJavaSourcesTask.class, task -> {
                    task.getPrivateDicomMetamodelYamlDirectory().set(extension.getPrivateDicomMetamodelYamlDirectory());
                    task.getGeneratedJavaOutputDirectory().set(extension.getGeneratedJavaOutputDirectory());
                });
        target.getPluginManager().withPlugin("java", javaPlugin -> {
            sourceSets.named("main", sourceSet -> sourceSet.getJava().srcDir(generateJavaSourcesFileTaskProvider));
            String version = target.getVersion().toString();
            target.getDependencies().add("api", "org.dcm4che:dcm4che-typeddicom-skeleton:" + version);
            target.getDependencies().add("implementation", "org.dcm4che:dcm4che-core");
        });
    }
}
