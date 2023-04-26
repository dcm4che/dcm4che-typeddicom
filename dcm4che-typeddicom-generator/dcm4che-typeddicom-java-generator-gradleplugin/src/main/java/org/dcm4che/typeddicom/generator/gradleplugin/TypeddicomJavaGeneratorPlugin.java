package org.dcm4che.typeddicom.generator.gradleplugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.plugins.ide.idea.model.IdeaModel;

public class TypeddicomJavaGeneratorPlugin implements Plugin<Project> {
    public void apply(final Project target) {
        final TypeddicomJavaGeneratorPluginExtension extension =
                target.getExtensions().create("generateTypeddicomJavaSources", TypeddicomJavaGeneratorPluginExtension.class);
        extension.getGeneratedJavaOutputDirectory().convention(target.getLayout().getBuildDirectory().dir("typeddicom"));
        extension.getPrivateDicomMetamodelYamlDirectory().convention(target.getLayout().getProjectDirectory().dir("src/main/resources"));
        TaskProvider<GenerateJavaSourcesTask> generateJavaSourcesFileTaskProvider =
                target.getTasks().register("generateJavaSourceFiles", GenerateJavaSourcesTask.class, task -> {
                    task.getPrivateDicomMetamodelYamlDirectory().set(extension.getPrivateDicomMetamodelYamlDirectory());
                    task.getGeneratedJavaOutputDirectory().set(extension.getGeneratedJavaOutputDirectory());
                });

        target.getPluginManager().withPlugin("java-library", javaPlugin -> {
            SourceSetContainer sourceSets = target.getExtensions().getByType(SourceSetContainer.class);
            sourceSets.named("main", sourceSet -> sourceSet.getJava().srcDir(generateJavaSourcesFileTaskProvider));
            String version = TypeddicomJavaGeneratorPlugin.class.getPackage().getImplementationVersion();
            target.getDependencies().add("api", "org.dcm4che:dcm4che-typeddicom-skeleton:" + version);
            target.getDependencies().add("implementation", "org.dcm4che:dcm4che-core");
        });

        target.getPluginManager().withPlugin("idea", ideaPlugin -> {
            IdeaModel ideaModel = target.getExtensions().getByType(IdeaModel.class);
            ideaModel.getModule().getGeneratedSourceDirs().add(extension.getGeneratedJavaOutputDirectory().getAsFile().get());
        });
    }
}
