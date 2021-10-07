package com.agfa.typeddicom.gradleplugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import com.agfa.typeddicom.DicomXmlParser;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPluginExtension;

public class ProcessDicomXmlPlugin implements Plugin<Project> {
    public void apply(final Project target) {
        final ProcessDicomXmlPluginExtension extension = target.getExtensions().create("processDicomXml", ProcessDicomXmlPluginExtension.class, new Object[0]);
        target.task("generateJavaSourceFiles", task -> {
            task.doLast(doLast -> {
                File javaDirectory = extension.getGeneratedJavaOutputDirectory().get().getAsFile();
                File dicomXmlDirectory = extension.getDicomStandardXmlDirectory().get().getAsFile();
                File mustacheTemplateDirectory = extension.getMustacheTemplateDirectory().get().getAsFile();

                target.getExtensions().getByType(JavaPluginExtension.class)
                        .getSourceSets()
                        .getByName("main")
                        .getJava()
                        .srcDirs(javaDirectory);

                if (javaDirectory.exists()) {
                    try {
                        Files.walkFileTree(javaDirectory.toPath(),
                                new SimpleFileVisitor<>() {
                                    @Override
                                    public FileVisitResult postVisitDirectory(
                                            Path dir, IOException exc) throws IOException {
                                        Files.delete(dir);
                                        return FileVisitResult.CONTINUE;
                                    }

                                    @Override
                                    public FileVisitResult visitFile(
                                            Path file, BasicFileAttributes attrs)
                                            throws IOException {
                                        Files.delete(file);
                                        return FileVisitResult.CONTINUE;
                                    }
                                });
                    } catch (IOException e) {
                        throw new RuntimeException("Couldn't delete java directory", e);
                    }
                }

                //noinspection ResultOfMethodCallIgnored
                javaDirectory.mkdirs();
                DicomXmlParser dicomXmlParser = new DicomXmlParser(dicomXmlDirectory, mustacheTemplateDirectory, javaDirectory);
                dicomXmlParser.generateSources();
            });
        });
    }
}
