package org.dcm4che.typeddicom.generator.gradleplugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.jvm.tasks.Jar;

public class GenerateTypeddicomJavaSourcesPlugin implements Plugin<Project> {
    public static final String BUNDLE_CONFIGURATION_NAME = "bundle";

    public void apply(final Project target) {
        final GenerateTypeddicomJavaSourcesPluginExtension extension = target.getExtensions().create("generateTypeddicomJavaSources", GenerateTypeddicomJavaSourcesPluginExtension.class);
        target.getTasks().register("generateJavaSourceFiles", GenerateJavaSourcesTask.class, task -> {
            task.getPrivateDicomMetamodelYamlDirectory().set(extension.getPrivateDicomMetamodelYamlDirectory());
            task.getGeneratedJavaOutputDirectory().set(extension.getGeneratedJavaOutputDirectory());
        });
        target.getConfigurations().create(BUNDLE_CONFIGURATION_NAME, (bundleConfiguration) ->
                target.getConfigurations().named("implementation").configure(implementationConfiguration ->
                        implementationConfiguration.extendsFrom(bundleConfiguration)
                )
        );
        target.getDependencies().add(BUNDLE_CONFIGURATION_NAME, "org.dcm4che:dcm4che-typeddicom-skeleton:$typeddicomVersion");
        target.getTasks().named("jar", Jar.class).configure(jar ->
                jar.from(target.getConfigurations().named(BUNDLE_CONFIGURATION_NAME))
        );
    }
}
