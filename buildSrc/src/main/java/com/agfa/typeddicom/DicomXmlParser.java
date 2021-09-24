package com.agfa.typeddicom;

import com.agfa.typeddicom.metamodel.DataElementMetaInfo;
import com.agfa.typeddicom.metamodel.ModuleMetaInfo;
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

/**
 * TODO describe this class
 *
 * @author Niklas Roth (niklas.roth@agfa.com)
 */
public class DicomXmlParser {
    private final SAXParser saxParser;
    private final DefaultMustacheFactory mustacheFactory;
    private final File dicomXmlDirectory;
    private final File mustacheTemplateDirectory;
    private final File javaOutputDirectory;

    public DicomXmlParser(File dicomXmlDirectory, File mustacheTemplateDirectory, File javaOutputDirectory) throws ParserConfigurationException, SAXException {
        this.dicomXmlDirectory = dicomXmlDirectory;
        this.mustacheTemplateDirectory = mustacheTemplateDirectory;
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
        DicomPart06Handler handlerPart6 = new DicomPart06Handler();
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

        generateDataElementWrapperClasses(dataElements, mustacheTemplateDirectory, javaOutputDirectory);
        generateModuleClasses(modules, mustacheTemplateDirectory, javaOutputDirectory);
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

    private void generateDataElementWrapperClasses(Set<DataElementMetaInfo> attributeMetaInfoMap, File mustacheTemplateDirectory, File javaOutputDirectory) throws IOException {
        Mustache mustache = mustacheFactory.compile("DataElement");
        File dataElementDir = new File(javaOutputDirectory, "com/agfa/typeddicom/dataelements");
        //noinspection ResultOfMethodCallIgnored
        dataElementDir.mkdirs();
        for (DataElementMetaInfo dataElementMetaInfo : attributeMetaInfoMap) {
            String filename = dataElementMetaInfo.getKeyword() + ".java";
            File javaFile = new File(dataElementDir, filename);
            try (FileWriter javaFileWriter = new FileWriter(javaFile)) {
                mustache.execute(javaFileWriter, dataElementMetaInfo);
            }
        }
    }

    private void generateModuleClasses(Set<ModuleMetaInfo> moduleMetaInfoSet, File mustacheTemplateDirectory, File javaOutputDirectory) throws IOException {
        Mustache mustache = mustacheFactory.compile( "Module");
        File dataElementDir = new File(javaOutputDirectory, "com/agfa/typeddicom/modules");
        //noinspection ResultOfMethodCallIgnored
        dataElementDir.mkdirs();
        for (ModuleMetaInfo moduleMetaInfo : moduleMetaInfoSet) {
            String filename = moduleMetaInfo.getKeyword() + "Module.java";
            File javaFile = new File(dataElementDir, filename);
            try (FileWriter javaFileWriter = new FileWriter(javaFile)) {
                mustache.execute(javaFileWriter, moduleMetaInfo);
            }
        }
    }


}
