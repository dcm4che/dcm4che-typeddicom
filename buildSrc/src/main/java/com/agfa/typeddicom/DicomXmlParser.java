package com.agfa.typeddicom;

import com.agfa.typeddicom.metamodel.DataElementMetaInfo;
import com.agfa.typeddicom.metamodel.InformationObjectDefinitionMetaInfo;
import com.agfa.typeddicom.metamodel.ModuleMetaInfo;
import com.agfa.typeddicom.metamodel.ValueRepresentationMetaInfo;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheNotFoundException;
import com.github.mustachejava.MustacheResolver;
import com.github.mustachejava.reflect.ReflectionObjectHandler;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
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

    public DicomXmlParser(File dicomXmlDirectory, File mustacheTemplateDirectory, File javaOutputDirectory) throws ParserConfigurationException, SAXException {
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
    }

    public void generateSources() throws IOException, SAXException {
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
        System.out.println("Generated " + valueRepresentations.size() + " value representation classes");

        generateValueRepresentationInterfaces(multiValueRepresentations);
        System.out.println("Generated " + multiValueRepresentations.size() + " multi value representation classes");

        generateDataElementWrapperClasses(dataElements);
        System.out.println("Generated " + dataElements.size() + " data element classes");

        generateModuleInterfaces(modules);
        System.out.println("Generated " + modules.size() + " module classes");

        generateIODClasses(iods);
        System.out.println("Generated " + iods.size() + " iod classes");
    }

    private void generateValueRepresentationInterfaces(Set<ValueRepresentationMetaInfo> valueRepresentations) throws IOException {
        Mustache mustache = mustacheFactory.compile("ValueRepresentation");
        File valueRepresentationDir = new File(javaOutputDirectory, "com/agfa/typeddicom/valuerepresentations");
        //noinspection ResultOfMethodCallIgnored
        valueRepresentationDir.mkdirs();
        for (ValueRepresentationMetaInfo valueRepresentation : valueRepresentations) {
            String filename = valueRepresentation.keyword() + JAVA_FILE_EXTENSION;
            File javaFile = new File(valueRepresentationDir, filename);
            try (FileWriter javaFileWriter = new FileWriter(javaFile)) {
                mustache.execute(javaFileWriter, valueRepresentation);
            }
        }
    }

    private void generateDataElementWrapperClasses(Set<DataElementMetaInfo> dataElements) throws IOException {
        Mustache mustache = mustacheFactory.compile("DataElement");
        File dataElementDir = new File(javaOutputDirectory, "com/agfa/typeddicom/dataelements");
        //noinspection ResultOfMethodCallIgnored
        dataElementDir.mkdirs();
        for (DataElementMetaInfo dataElementMetaInfo : dataElements) {
            String filename = dataElementMetaInfo.getKeyword() + JAVA_FILE_EXTENSION;
            File javaFile = new File(dataElementDir, filename);
            try (FileWriter javaFileWriter = new FileWriter(javaFile)) {
                mustache.execute(javaFileWriter, dataElementMetaInfo);
            }
        }
    }

    private void generateIODClasses(Set<InformationObjectDefinitionMetaInfo> iods) throws IOException {
        Mustache mustache = mustacheFactory.compile("InformationObjectDefinition");
        File dataElementDir = new File(javaOutputDirectory, "com/agfa/typeddicom/iods");
        //noinspection ResultOfMethodCallIgnored
        dataElementDir.mkdirs();
        for (InformationObjectDefinitionMetaInfo iod : iods) {
            String filename = iod.getKeyword() + JAVA_FILE_EXTENSION;
            File javaFile = new File(dataElementDir, filename);
            try (FileWriter javaFileWriter = new FileWriter(javaFile)) {
                mustache.execute(javaFileWriter, iod);
            }
        }
    }

    private void generateModuleInterfaces(Set<ModuleMetaInfo> moduleMetaInfoSet) throws IOException {
        Mustache mustache = mustacheFactory.compile("Module");
        File dataElementDir = new File(javaOutputDirectory, "com/agfa/typeddicom/modules");
        //noinspection ResultOfMethodCallIgnored
        dataElementDir.mkdirs();
        for (ModuleMetaInfo moduleMetaInfo : moduleMetaInfoSet) {
            String filename = moduleMetaInfo.getKeyword() + JAVA_FILE_EXTENSION;
            File javaFile = new File(dataElementDir, filename);
            try (FileWriter javaFileWriter = new FileWriter(javaFile)) {
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
