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

public abstract class GenerateJavaSourcesTask extends DefaultTask {
    @InputDirectory
    public abstract DirectoryProperty getPrivateDicomMetamodelYamlDirectory();

    @OutputDirectory
    public abstract DirectoryProperty getGeneratedJavaOutputDirectory();

    @TaskAction
    public void generateJavaSources() {
        List<File> privateMetamodelYamlFiles = new LinkedList<>();
        Directory privateDicomMetamodelYamlDirectory = this.getPrivateDicomMetamodelYamlDirectory().getOrNull();
        if (privateDicomMetamodelYamlDirectory != null) {
            privateMetamodelYamlFiles.addAll(privateDicomMetamodelYamlDirectory.getAsFileTree().getFiles());
        }
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
        JavaGenerator javaGenerator = new JavaGenerator("std.dicom-meta-model.yaml", privateMetamodelYamlFiles, javaDirectory);
        javaGenerator.generateSources();
    }
}
