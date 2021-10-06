package com.agfa.typeddicom;

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory

abstract class ProcessDicomXmlPluginExtension {
    @get:OutputDirectory
    abstract val dicomStandardXmlDirectory: DirectoryProperty

    @get:InputDirectory
    abstract val mustacheTemplateDirectory: DirectoryProperty

    @get:InputDirectory
    abstract val generatedJavaOutputDirectory: DirectoryProperty
}
