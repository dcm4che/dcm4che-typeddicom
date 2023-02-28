package org.dcm4che.typeddicom.generator.gradleplugin;

import org.dcm4che.typeddicom.generator.JavaGenerator;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.Directory;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.tasks.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;

abstract public class GenerateJavaSourcesTask extends DefaultTask {
    public static final String METAMODEL_SOURCE_SET_NAME = "metamodel";

    @InputDirectory
    abstract public DirectoryProperty getPrivateDicomMetamodelYamlDirectory();

    @InputDirectory
    abstract public DirectoryProperty getMustacheTemplateDirectory();

    @OutputDirectory
    abstract public DirectoryProperty getGeneratedJavaOutputDirectory();

    @TaskAction
    public void generateJavaSources() {
        SourceSet metamodelSourceSet = getProject().getExtensions().getByType(SourceSetContainer.class).getByName(METAMODEL_SOURCE_SET_NAME);
        List<File> metamodelYamlFiles = new LinkedList<>(metamodelSourceSet.getAllSource().getFiles());
        Directory privateDicomMetamodelYamlDirectory2 = this.getPrivateDicomMetamodelYamlDirectory().get();
        if (privateDicomMetamodelYamlDirectory2 != null) {
            metamodelYamlFiles.addAll(privateDicomMetamodelYamlDirectory2.getAsFileTree().getFiles());
        }
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
        JavaGenerator dicomXmlParser = new JavaGenerator(metamodelYamlFiles, mustacheTemplateDirectory, javaDirectory);
        dicomXmlParser.generateSources();
    }
}
