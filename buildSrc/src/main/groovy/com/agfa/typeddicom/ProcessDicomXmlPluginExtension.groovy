package com.agfa.typeddicom

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory

/**
 * TODO describe this class
 *
 * @author Niklas Roth
 */
interface ProcessDicomXmlPluginExtension {
    @InputDirectory
    DirectoryProperty getDicomStandardXmlDirectory()

    @InputDirectory
    DirectoryProperty getMustacheTemplateDirectory()

    @OutputDirectory
    DirectoryProperty getGeneratedJavaOutputDirectory()
}
