package com.agfa.typeddicom.gradleplugin;

import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputDirectory;

public interface ProcessDicomXmlPluginExtension {
    @InputDirectory
    DirectoryProperty getDicomStandardXmlDirectory();

    @InputDirectory
    DirectoryProperty getMustacheTemplateDirectory();

    @OutputDirectory
    DirectoryProperty getGeneratedJavaOutputDirectory();
}