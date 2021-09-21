package com.agfa.typeddicom


import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * TODO describe this class
 *
 * @author Niklas Roth
 */
class ProcessDicomXmlPlugin implements Plugin<Project> {
    @Override
    void apply(Project target) {
        ProcessDicomXmlPluginExtension extension = target.extensions.create('processDicomXml', ProcessDicomXmlPluginExtension)
        target.task('generateJavaSourceFiles') {
            doLast {
                File javaDirectory = extension.getGeneratedJavaOutputDirectory().get().asFile
                File dicomXmlDirectory = extension.dicomStandardXmlDirectory.get().asFile
                File mustacheTemplateDirectory = extension.mustacheTemplateDirectory.get().asFile
                target.sourceSets.main.java.srcDirs += javaDirectory

                if (javaDirectory.exists()) {
                    javaDirectory.deleteDir()
                }
                javaDirectory.mkdirs()
                DicomXmlParser dicomXmlParser = new DicomXmlParser()
                dicomXmlParser.generateSources(dicomXmlDirectory, mustacheTemplateDirectory, javaDirectory)
            }
        }
    }
}
