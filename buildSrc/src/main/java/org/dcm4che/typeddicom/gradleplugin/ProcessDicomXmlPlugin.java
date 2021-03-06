package org.dcm4che.typeddicom.gradleplugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class ProcessDicomXmlPlugin implements Plugin<Project> {
    public void apply(final Project target) {
        final ProcessDicomXmlPluginExtension extension = target.getExtensions().create("processDicomXml", ProcessDicomXmlPluginExtension.class);
        target.getTasks().register("generateJavaSourceFiles", GenerateJavaSourcesTask.class, task -> {
            task.getMustacheTemplateDirectory().set(extension.getMustacheTemplateDirectory());
            task.getDicomStandardXmlDirectory().set(extension.getDicomStandardXmlDirectory());
            task.getGeneratedJavaOutputDirectory().set(extension.getGeneratedJavaOutputDirectory());
        });
    }
}
