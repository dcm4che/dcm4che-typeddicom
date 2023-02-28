package org.dcm4che.typeddicom.generator.gradleplugin;

import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputDirectory;

public interface GenerateTypeddicomJavaSourcesPluginExtension {
    @InputDirectory
    DirectoryProperty getPrivateDicomMetamodelYamlDirectory();

    @InputDirectory
    DirectoryProperty getMustacheTemplateDirectory();

    @OutputDirectory
    DirectoryProperty getGeneratedJavaOutputDirectory();
}
