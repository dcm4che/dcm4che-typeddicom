package org.dcm4che.typeddicom.generator.gradleplugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class GenerateTypeddicomJavaSourcesPlugin implements Plugin<Project> {
    public void apply(final Project target) {
        final GenerateTypeddicomJavaSourcesPluginExtension extension = target.getExtensions().create("generateTypeddicomJavaSources", GenerateTypeddicomJavaSourcesPluginExtension.class);
        target.getTasks().register("generateJavaSourceFiles", GenerateJavaSourcesTask.class, task -> {
            task.getPrivateDicomMetamodelYamlDirectory().set(extension.getPrivateDicomMetamodelYamlDirectory());
            task.getGeneratedJavaOutputDirectory().set(extension.getGeneratedJavaOutputDirectory());
        });
        String version = target.getVersion().toString();
        target.getDependencies().add("api", "org.dcm4che:dcm4che-typeddicom-skeleton:" + version);
    }
}
