package org.dcm4che.typeddicom.generator.gradleplugin;

import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.tasks.Optional;

public interface TypeddicomJavaGeneratorPluginExtension {
    @Optional
    DirectoryProperty getPrivateDicomMetamodelYamlDirectory();

    DirectoryProperty getGeneratedJavaOutputDirectory();
}
