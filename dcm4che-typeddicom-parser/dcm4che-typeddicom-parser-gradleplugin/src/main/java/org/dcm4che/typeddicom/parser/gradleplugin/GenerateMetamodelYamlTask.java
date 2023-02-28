package org.dcm4che.typeddicom.parser.gradleplugin;

import org.dcm4che.typeddicom.parser.DicomXmlParser;
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

abstract public class GenerateMetamodelYamlTask extends DefaultTask {
    @InputDirectory
    abstract public DirectoryProperty getDicomStandardXmlDirectory();

    @OutputDirectory
    abstract public DirectoryProperty getGeneratedYamlOutputDirectory();

    @TaskAction
    public void generateJavaSources() {
        File dicomXmlDirectory = this.getDicomStandardXmlDirectory().get().getAsFile();
        File javaDirectory = this.getGeneratedYamlOutputDirectory().get().getAsFile();


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
        DicomXmlParser dicomXmlParser = new DicomXmlParser(dicomXmlDirectory, javaDirectory);
        dicomXmlParser.generateSources();
    }
}
