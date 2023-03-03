package org.dcm4che.typeddicom.parser.gradleplugin;

import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputDirectory;

public interface ProcessDicomXmlPluginExtension {
    DirectoryProperty getDicomStandardXmlDirectory();

    DirectoryProperty getGeneratedYamlMetamodelOutputDirectory();
}
