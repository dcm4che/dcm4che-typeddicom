package org.dcm4che.typeddicom.parser.gradleplugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;

public class ProcessDicomXmlPlugin implements Plugin<Project> {
    public void apply(final Project target) {
        final ProcessDicomXmlPluginExtension extension = target.getExtensions().create("processDicomXml", ProcessDicomXmlPluginExtension.class);
        target.getTasks().register("generateYamlFiles", GenerateMetamodelYamlTask.class, task -> {
            task.getDicomStandardXmlDirectory().set(extension.getDicomStandardXmlDirectory());
            task.getGeneratedYamlOutputDirectory().set(extension.getGeneratedYamlMetamodelOutputDirectory());
            SourceSetContainer sourceSets = target.getExtensions().getByType(JavaPluginExtension.class).getSourceSets();
            SourceDirectorySet resourcesSourceSet = sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME).getResources();
            resourcesSourceSet.srcDir(extension.getGeneratedYamlMetamodelOutputDirectory());
        });
    }
}
