package org.dcm4che.typeddicom.generator.gradleplugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class GenerateTypeddicomJavaSourcesPlugin implements Plugin<Project> {
    public void apply(final Project target) {
        final GenerateTypeddicomJavaSourcesPluginExtension extension = target.getExtensions().create("generateTypeddicomJavaSources", GenerateTypeddicomJavaSourcesPluginExtension.class);
        target.getTasks().register("generateJavaSourceFiles", GenerateJavaSourcesTask.class, task -> {
            task.getMustacheTemplateDirectory().set(extension.getMustacheTemplateDirectory());
            task.getPrivateDicomMetamodelYamlDirectory().set(extension.getPrivateDicomMetamodelYamlDirectory());
            task.getGeneratedJavaOutputDirectory().set(extension.getGeneratedJavaOutputDirectory());
        });
    }
}
