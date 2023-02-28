package org.dcm4che.typeddicom.generator;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheNotFoundException;
import com.github.mustachejava.MustacheResolver;
import com.github.mustachejava.reflect.ReflectionObjectHandler;
import org.dcm4che.typeddicom.parser.metamodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JavaGenerator {
    public static final String JAVA_FILE_EXTENSION = ".java";
    private static final Logger LOGGER = LoggerFactory.getLogger(JavaGenerator.class);

    private final DefaultMustacheFactory mustacheFactory;
    private final List<File> privateDicomMetamodelYamlDirectory;
    private final File javaOutputDirectory;

    public JavaGenerator(List<File> privateDicomMetamodelYamlDirectory, File mustacheTemplateDirectory, File javaOutputDirectory) {
        this.privateDicomMetamodelYamlDirectory = privateDicomMetamodelYamlDirectory;
        this.javaOutputDirectory = javaOutputDirectory;
        MustacheResolver mustacheResolver = resourceName -> {
            File mustacheFile = new File(mustacheTemplateDirectory, resourceName + ".java.mustache");
            try {
                return new FileReader(mustacheFile);
            } catch (FileNotFoundException e) {
                throw new MustacheNotFoundException(resourceName, e);
            }
        };
        mustacheFactory = new DefaultMustacheFactory(mustacheResolver);
        mustacheFactory.setObjectHandler(new MapMethodReflectionHandler());
    }

    public void generateSources() {
        try {
            DicomMetaModel dicomMetaModel = loadYamlFiles(privateDicomMetamodelYamlDirectory);

            generateJavaSources(dicomMetaModel);
        } catch (IOException e) {
            throw new RuntimeException("Wasn't able to generate the Sources", e);
        }
    }

    private DicomMetaModel loadYamlFiles(List<File> yamlSourceDirectories) {
        // TODO: implement this
        return new DicomMetaModel();
    }

    private void generateJavaSources(DicomMetaModel dicomMetaModel) throws IOException {
        generateValueRepresentationInterfaces(dicomMetaModel.getValueRepresentations());
        LOGGER.info("Generated {} Value Representation classes", dicomMetaModel.getValueRepresentations().size());

        generateDataElementWrapperClasses(dicomMetaModel.getDataElements());
        LOGGER.info("Generated {} Data Element classes", dicomMetaModel.getDataElements().size());

        generateModuleInterfaces(dicomMetaModel.getModules());
        LOGGER.info("Generated {} Module classes", dicomMetaModel.getModules().size());

        generateIODClasses(dicomMetaModel.getIods());
        LOGGER.info("Generated {} IOD classes", dicomMetaModel.getIods().size());
    }

    private void generateValueRepresentationInterfaces(Set<ValueRepresentationMetaInfo> valueRepresentations) throws IOException {
        Mustache mustache = mustacheFactory.compile("ValueRepresentation");
        File valueRepresentationDir = new File(javaOutputDirectory, "org/dcm4che/typeddicom/valuerepresentations");
        //noinspection ResultOfMethodCallIgnored
        valueRepresentationDir.mkdirs();
        for (ValueRepresentationMetaInfo valueRepresentation : valueRepresentations) {
            String filename = valueRepresentation.keyword() + JAVA_FILE_EXTENSION;
            File javaFile = new File(valueRepresentationDir, filename);
            try (FileWriter javaFileWriter = new FileWriter(javaFile, StandardCharsets.UTF_8)) {
                mustache.execute(javaFileWriter, valueRepresentation);
            }
        }
    }

    private void generateDataElementWrapperClasses(Set<DataElementMetaInfo> dataElements) throws IOException {
        Mustache mustache = mustacheFactory.compile("DataElement");
        File dataElementDir = new File(javaOutputDirectory, "org/dcm4che/typeddicom/dataelements");
        //noinspection ResultOfMethodCallIgnored
        dataElementDir.mkdirs();
        for (DataElementMetaInfo dataElementMetaInfo : dataElements) {
            String filename = dataElementMetaInfo.getKeyword() + JAVA_FILE_EXTENSION;
            File javaFile = new File(dataElementDir, filename);
            try (FileWriter javaFileWriter = new FileWriter(javaFile, StandardCharsets.UTF_8)) {
                mustache.execute(javaFileWriter, dataElementMetaInfo);
            }
        }
    }

    private void generateIODClasses(Set<InformationObjectDefinitionMetaInfo> iods) throws IOException {
        Mustache mustache = mustacheFactory.compile("InformationObjectDefinition");
        File dataElementDir = new File(javaOutputDirectory, "org/dcm4che/typeddicom/iods");
        //noinspection ResultOfMethodCallIgnored
        dataElementDir.mkdirs();
        for (InformationObjectDefinitionMetaInfo iod : iods) {
            String filename = iod.getKeyword() + JAVA_FILE_EXTENSION;
            File javaFile = new File(dataElementDir, filename);
            try (FileWriter javaFileWriter = new FileWriter(javaFile, StandardCharsets.UTF_8)) {
                mustache.execute(javaFileWriter, iod);
            }
        }
    }

    private void generateModuleInterfaces(Set<ModuleMetaInfo> moduleMetaInfoSet) throws IOException {
        Mustache mustache = mustacheFactory.compile("Module");
        File dataElementDir = new File(javaOutputDirectory, "org/dcm4che/typeddicom/modules");
        //noinspection ResultOfMethodCallIgnored
        dataElementDir.mkdirs();
        for (ModuleMetaInfo moduleMetaInfo : moduleMetaInfoSet) {
            String filename = moduleMetaInfo.getKeyword() + JAVA_FILE_EXTENSION;
            File javaFile = new File(dataElementDir, filename);
            try (FileWriter javaFileWriter = new FileWriter(javaFile, StandardCharsets.UTF_8)) {
                mustache.execute(javaFileWriter, moduleMetaInfo);
            }
        }
    }

    /**
     * Extended reflection handler that can access map methods.
     */
    private static class MapMethodReflectionHandler extends ReflectionObjectHandler {

        @Override
        protected boolean areMethodsAccessible(Map<?, ?> map) {
            return true;
        }
    }


}