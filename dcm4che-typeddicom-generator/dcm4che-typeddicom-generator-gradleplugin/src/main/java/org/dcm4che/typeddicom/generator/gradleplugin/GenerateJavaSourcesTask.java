package org.dcm4che.typeddicom.generator.gradleplugin;

import org.dcm4che.typeddicom.generator.DicomXmlParser;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

abstract public class GenerateJavaSourcesTask extends DefaultTask {
    @InputDirectory
    abstract public DirectoryProperty getDicomStandardXmlDirectory();

    @InputDirectory
    abstract public DirectoryProperty getMustacheTemplateDirectory();

    @OutputDirectory
    abstract public DirectoryProperty getGeneratedJavaOutputDirectory();

    @TaskAction
    public void generateJavaSources() {
        File dicomXmlDirectory = this.getDicomStandardXmlDirectory().get().getAsFile();
        File mustacheTemplateDirectory = this.getMustacheTemplateDirectory().get().getAsFile();
        File javaDirectory = this.getGeneratedJavaOutputDirectory().get().getAsFile();


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
    }
}
