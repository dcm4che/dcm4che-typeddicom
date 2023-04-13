package org.dcm4che.typeddicom.parser.gradleplugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.ProjectLayout;

public class DicomXmlParserPlugin implements Plugin<Project> {
    public void apply(final Project target) {
        final DicomXmlParserPluginExtension extension = target.getExtensions().create("processDicomXml", DicomXmlParserPluginExtension.class);
        ProjectLayout layout = target.getLayout();
        extension.getDicomStandardXmlDirectory().convention(layout.getProjectDirectory().dir("src/main/resources/dicom-standard-xml"));
        extension.getGeneratedYamlMetamodelOutputDirectory().convention(layout.getBuildDirectory().dir("typeddicom-generated/resources"));
        target.getTasks().register("generateYamlFiles", GenerateMetamodelYamlTask.class, task -> {
            task.getDicomStandardXmlDirectory().set(extension.getDicomStandardXmlDirectory());
            task.getGeneratedYamlMetamodelOutputDirectory().set(extension.getGeneratedYamlMetamodelOutputDirectory());
        });
    }
}
