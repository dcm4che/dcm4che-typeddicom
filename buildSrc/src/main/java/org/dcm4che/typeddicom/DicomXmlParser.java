package org.dcm4che.typeddicom;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheNotFoundException;
import com.github.mustachejava.MustacheResolver;
import com.github.mustachejava.reflect.ReflectionObjectHandler;
import org.dcm4che.typeddicom.metamodel.DataElementMetaInfo;
import org.dcm4che.typeddicom.metamodel.InformationObjectDefinitionMetaInfo;
import org.dcm4che.typeddicom.metamodel.ModuleMetaInfo;
import org.dcm4che.typeddicom.metamodel.ValueRepresentationMetaInfo;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * TODO describe this class
 *
 * @author Niklas Roth (niklas.roth@agfa.com)
 */
public class DicomXmlParser {
    public static final String JAVA_FILE_EXTENSION = ".java";
    private final SAXParser saxParser;
    private final DefaultMustacheFactory mustacheFactory;
    private final File dicomXmlDirectory;
    private final File javaOutputDirectory;

    public DicomXmlParser(File dicomXmlDirectory, File mustacheTemplateDirectory, File javaOutputDirectory) {
        try {
            this.dicomXmlDirectory = dicomXmlDirectory;
            this.javaOutputDirectory = javaOutputDirectory;
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            saxParser = saxParserFactory.newSAXParser();
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
        } catch (ParserConfigurationException | SAXException e) {
            throw new RuntimeException("Wasn't able to create the DICOM XML Parser", e);
        }
    }

    public void generateSources() {
        try {
            DicomPart05Handler handlerPart5 = new DicomPart05Handler();
            saxParser.parse(new File(dicomXmlDirectory, "part05.xml"), handlerPart5);
            Set<ValueRepresentationMetaInfo> valueRepresentations = handlerPart5.getValueRepresentations();
            Set<ValueRepresentationMetaInfo> multiValueRepresentations = handlerPart5.getMultiValueRepresentations();

            Map<String, ValueRepresentationMetaInfo> valueRepresentationsMap =
                    valueRepresentations.stream().collect(Collectors.toMap(
                            ValueRepresentationMetaInfo::tag,
                            Function.identity())
                    );

            Map<String, ValueRepresentationMetaInfo> multiValueRepresentationsMap =
                    multiValueRepresentations.stream().collect(Collectors.toMap(
                            ValueRepresentationMetaInfo::tag,
                            Function.identity())
                    );

            DicomPart06Handler handlerPart6 = new DicomPart06Handler(valueRepresentationsMap, multiValueRepresentationsMap);
            saxParser.parse(new File(dicomXmlDirectory, "part06.xml"), handlerPart6);
            Set<DataElementMetaInfo> dataElements = handlerPart6.getDataElements();

            Map<String, Set<DataElementMetaInfo>> dataElementMetaInfoMap = new HashMap<>();
            for (DataElementMetaInfo dataElement : dataElements) {
                dataElementMetaInfoMap.computeIfAbsent(dataElement.getTag(), (key) -> new HashSet<>());
                dataElementMetaInfoMap.get(dataElement.getTag()).add(dataElement);
            }

            DicomPart03Handler handlerPart3 = new DicomPart03Handler(dataElementMetaInfoMap);
            saxParser.parse(new File(dicomXmlDirectory, "part03.xml"), handlerPart3);
            Set<ModuleMetaInfo> modules = handlerPart3.getModules();
            Set<InformationObjectDefinitionMetaInfo> iods = handlerPart3.getIODs();

            generateValueRepresentationInterfaces(valueRepresentations);
            System.out.println("Generated " + valueRepresentations.size() + " Value Representation classes");

            generateValueRepresentationInterfaces(multiValueRepresentations);
            System.out.println("Generated " + multiValueRepresentations.size() + " Multi Value Representation classes");

            generateDataElementWrapperClasses(dataElements);
            System.out.println("Generated " + dataElements.size() + " Data Element classes");

            generateModuleInterfaces(modules);
            System.out.println("Generated " + modules.size() + " Module classes");

            generateIODClasses(iods);
            System.out.println("Generated " + iods.size() + " IOD classes");
        } catch (SAXException | IOException e) {
            throw new RuntimeException("Wasn't able to generate the Sources", e);
        }
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
