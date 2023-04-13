package org.dcm4che.typeddicom.generator;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheNotFoundException;
import com.github.mustachejava.MustacheResolver;
import com.github.mustachejava.reflect.ReflectionObjectHandler;
import org.dcm4che.typeddicom.generator.model.mustache.DataElementMustacheModel;
import org.dcm4che.typeddicom.generator.model.mustache.InformationObjectDefinitionMustacheModel;
import org.dcm4che.typeddicom.parser.metamodel.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.LoaderOptions;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class JavaGenerator {
    public static final String JAVA_FILE_EXTENSION = ".java";
    private static final Logger LOGGER = LoggerFactory.getLogger(JavaGenerator.class);
    public static final int MAX_YAML_FILE_SIZE = 15<<20; // 15 MiB

    private final DefaultMustacheFactory mustacheFactory;
    private final String stdMetaModelYamlFileResourcePath;
    private final List<File> privateMetamodelYamlFiles;
    private final File javaOutputDirectory;
    private final YAMLMapper yamlMapper;

    public JavaGenerator(String stdMetaModelYamlFileResourcePath, List<File> privateMetamodelYamlFiles, File javaOutputDirectory) {
        this.stdMetaModelYamlFileResourcePath = stdMetaModelYamlFileResourcePath;
        this.privateMetamodelYamlFiles = privateMetamodelYamlFiles;
        this.javaOutputDirectory = javaOutputDirectory;
        MustacheResolver mustacheResolver = resourceName -> getInputStreamReaderForTemplate(resourceName);
        mustacheFactory = new DefaultMustacheFactory(mustacheResolver);
        mustacheFactory.setObjectHandler(new MapMethodReflectionHandler());

        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setCodePointLimit(MAX_YAML_FILE_SIZE);
        yamlMapper = new YAMLMapper(YAMLFactory.builder().loaderOptions(loaderOptions).build());
    }

    private InputStreamReader getInputStreamReaderForTemplate(String resourceName) {
        InputStream mustacheFile = this.getClass().getClassLoader().getResourceAsStream("templates" + "/" + resourceName + ".java.mustache");
        if (mustacheFile == null) {
            throw new MustacheNotFoundException(resourceName);
        } else {
            return new InputStreamReader(mustacheFile);
        }
    }

    public void generateSources() {
        try {
            DicomMetaModelDTO dicomMetaModelDTO = loadYamlFiles(privateMetamodelYamlFiles);
            generateJavaSources(dicomMetaModelDTO);
        } catch (IOException e) {
            throw new RuntimeException("Wasn't able to generate the Sources", e);
        }
    }

    private DicomMetaModelDTO loadYamlFiles(List<File> privateMetamodelYamlFiles) throws IOException {
        DicomMetaModelDTO dicomMetaModelDTO = loadStdMetaModel();
        updateWithPrivateMetaModelYamlFiles(dicomMetaModelDTO, privateMetamodelYamlFiles);
        return dicomMetaModelDTO;
    }

    private DicomMetaModelDTO loadStdMetaModel() throws IOException {
        DicomMetaModelDTO dicomMetaModelDTO;
        try (InputStream stdMetaModelYamlInputStream = this.getClass().getClassLoader().getResourceAsStream(stdMetaModelYamlFileResourcePath)) {
            dicomMetaModelDTO = this.yamlMapper.readValue(stdMetaModelYamlInputStream, DicomMetaModelDTO.class);
        }
        return dicomMetaModelDTO;
    }

    private void updateWithPrivateMetaModelYamlFiles(DicomMetaModelDTO dicomMetaModelDTO, List<File> privateMetamodelYamlFiles) throws IOException {
        ObjectReader updateReader = this.yamlMapper.readerForUpdating(dicomMetaModelDTO);
        for (File metamodelYamlFile : privateMetamodelYamlFiles) {
            updateReader.readValue(metamodelYamlFile);
        }
    }

    private void generateJavaSources(DicomMetaModelDTO dicomMetaModelDTO) throws IOException {
        generateValueRepresentationSingleInterfaces(dicomMetaModelDTO.valueRepresentations());
        generateValueRepresentationMultiInterfaces(dicomMetaModelDTO.valueRepresentations());
        LOGGER.info("Generated {} Value Representation interfaces", dicomMetaModelDTO.valueRepresentations().size() * 2);

        generateDataElementWrapperClasses(dicomMetaModelDTO.dataElements());
        LOGGER.info("Generated {} Data Element classes", dicomMetaModelDTO.dataElements().size());

        generateModuleInterfaces(dicomMetaModelDTO.modules());
        LOGGER.info("Generated {} Module interfaces", dicomMetaModelDTO.modules().size());

        generateIODClasses(dicomMetaModelDTO.iods());
        LOGGER.info("Generated {} IOD classes", dicomMetaModelDTO.iods().size());
    }

    private void generateValueRepresentationSingleInterfaces(Map<String, ValueRepresentationMetaInfoDTO> valueRepresentations) throws IOException {
        Mustache mustache = mustacheFactory.compile("ValueRepresentationSingle");
        File valueRepresentationDir = new File(javaOutputDirectory, "org/dcm4che/typeddicom/valuerepresentations");
        //noinspection ResultOfMethodCallIgnored
        valueRepresentationDir.mkdirs();
        for (Map.Entry<String, ValueRepresentationMetaInfoDTO> valueRepresentationEntry : valueRepresentations.entrySet()) {
            String filename = valueRepresentationEntry.getKey() + "Wrapper" + JAVA_FILE_EXTENSION;
            File javaFile = new File(valueRepresentationDir, filename);
            try (FileWriter javaFileWriter = new FileWriter(javaFile, StandardCharsets.UTF_8)) {
                mustache.execute(javaFileWriter, valueRepresentationEntry);
            }
        }
    }

    private void generateValueRepresentationMultiInterfaces(Map<String, ValueRepresentationMetaInfoDTO> valueRepresentations) throws IOException {
        Mustache mustache = mustacheFactory.compile("ValueRepresentationMulti");
        File valueRepresentationDir = new File(javaOutputDirectory, "org/dcm4che/typeddicom/valuerepresentations");
        //noinspection ResultOfMethodCallIgnored
        valueRepresentationDir.mkdirs();
        for (Map.Entry<String, ValueRepresentationMetaInfoDTO> valueRepresentationEntry : valueRepresentations.entrySet()) {
            String filename = valueRepresentationEntry.getKey() + "MultiWrapper" + JAVA_FILE_EXTENSION;
            File javaFile = new File(valueRepresentationDir, filename);
            try (FileWriter javaFileWriter = new FileWriter(javaFile, StandardCharsets.UTF_8)) {
                mustache.execute(javaFileWriter, valueRepresentationEntry);
            }
        }
    }

    private void generateDataElementWrapperClasses(Map<String, DataElementMetaInfoDTO> dataElements) throws IOException {
        Mustache mustache = mustacheFactory.compile("DataElement");
        File dataElementDir = new File(javaOutputDirectory, "org/dcm4che/typeddicom/dataelements");
        //noinspection ResultOfMethodCallIgnored
        dataElementDir.mkdirs();
        for (Map.Entry<String, DataElementMetaInfoDTO> dataElementMetaInfoEntry : dataElements.entrySet()) {
            String filename = dataElementMetaInfoEntry.getKey() + JAVA_FILE_EXTENSION;
            File javaFile = new File(dataElementDir, filename);
            try (FileWriter javaFileWriter = new FileWriter(javaFile, StandardCharsets.UTF_8)) {
                mustache.execute(javaFileWriter, new DataElementMustacheModel(dataElementMetaInfoEntry.getKey(), dataElementMetaInfoEntry.getValue()));
            }
        }
    }

    private void generateIODClasses(Map<String, InformationObjectDefinitionMetaInfoDTO> iods) throws IOException {
        Mustache mustache = mustacheFactory.compile("InformationObjectDefinition");
        File dataElementDir = new File(javaOutputDirectory, "org/dcm4che/typeddicom/iods");
        //noinspection ResultOfMethodCallIgnored
        dataElementDir.mkdirs();
        for (Map.Entry<String, InformationObjectDefinitionMetaInfoDTO> iodEntry : iods.entrySet()) {
            String filename = iodEntry.getKey() + JAVA_FILE_EXTENSION;
            File javaFile = new File(dataElementDir, filename);
            try (FileWriter javaFileWriter = new FileWriter(javaFile, StandardCharsets.UTF_8)) {
                mustache.execute(javaFileWriter, new InformationObjectDefinitionMustacheModel(iodEntry.getKey(), iodEntry.getValue()));
            }
        }
    }

    private void generateModuleInterfaces(Map<String, ModuleMetaInfoDTO> moduleMetaInfoSet) throws IOException {
        Mustache mustache = mustacheFactory.compile("Module");
        File dataElementDir = new File(javaOutputDirectory, "org/dcm4che/typeddicom/modules");
        //noinspection ResultOfMethodCallIgnored
        dataElementDir.mkdirs();
        for (Map.Entry<String, ModuleMetaInfoDTO> moduleMetaInfo : moduleMetaInfoSet.entrySet()) {
            String filename = moduleMetaInfo.getKey() + JAVA_FILE_EXTENSION;
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
