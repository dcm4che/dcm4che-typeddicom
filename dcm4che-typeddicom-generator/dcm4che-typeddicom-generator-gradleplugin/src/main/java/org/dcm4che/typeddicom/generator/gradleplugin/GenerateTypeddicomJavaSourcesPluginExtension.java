package org.dcm4che.typeddicom.generator.gradleplugin;

import groovy.lang.Closure;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.OutputDirectory;

import java.io.File;

public interface GenerateTypeddicomJavaSourcesPluginExtension {
    @Optional
    @InputDirectory
    DirectoryProperty getPrivateDicomMetamodelYamlDirectory();

    @OutputDirectory
    DirectoryProperty getGeneratedJavaOutputDirectory();
}
