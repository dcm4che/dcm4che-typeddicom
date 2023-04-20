package org.dcm4che.typeddicom.parser.gradleplugin;

import org.gradle.api.file.DirectoryProperty;

public interface DicomXmlParserPluginExtension {
    DirectoryProperty getDicomStandardXmlDirectory();

    DirectoryProperty getGeneratedYamlMetamodelOutputDirectory();
}
