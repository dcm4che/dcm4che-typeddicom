package com.agfa.typeddicom;

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

open class ProcessDicomXmlPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val extension: ProcessDicomXmlPluginExtension = target.extensions.create("processDicomXml", ProcessDicomXmlPluginExtension::class.java)
        target.task("generateJavaSourceFiles") {
            doLast {
                val javaDirectory: File = extension.generatedJavaOutputDirectory.get().asFile
                val dicomXmlDirectory: File = extension.dicomStandardXmlDirectory.get().asFile
                val mustacheTemplateDirectory: File = extension.mustacheTemplateDirectory.get().asFile
                // target.sourceSets.main.java.srcDirs += javaDirectory

                if (javaDirectory.exists()) {
                    javaDirectory.deleteRecursively()
                }
                javaDirectory.mkdirs()
                val dicomXmlParser = DicomXmlParser(dicomXmlDirectory, mustacheTemplateDirectory, javaDirectory)
                dicomXmlParser.generateSources()
            }
        }
    }
}