package com.agfa.typeddicom;

import com.agfa.typeddicom.metamodel.DataElementMetaInfo;
import com.agfa.typeddicom.metamodel.ModuleMetaInfo;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

    public DicomXmlParser() throws ParserConfigurationException, SAXException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        saxParser = saxParserFactory.newSAXParser();
    }

    public void generateSources(File dicomXmlDirectory, File mustacheTemplateDirectory, File javaOutputDirectory) throws IOException, SAXException, ParserConfigurationException {
        DicomPart06Handler handlerPart6 = new DicomPart06Handler();
        saxParser.parse(new File(dicomXmlDirectory, "part06.xml"), handlerPart6);
        Set<DataElementMetaInfo> dataElements = handlerPart6.getDataElements();

        //Map<String, DataElementMetaInfo> dataElementMetaInfoMap = dataElements.stream()
        //        .collect(Collectors.toMap(DataElementMetaInfo::getTag, Function.identity()));

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

    private void generateDataElementWrapperClasses(Set<DataElementMetaInfo> attributeMetaInfoMap, File mustacheTemplateDirectory, File javaOutputDirectory) throws IOException {
        MustacheFactory mustacheFactory = new DefaultMustacheFactory();
        String mustacheFilename = "DataElement.java.mustache";
        File mustacheFile = new File(mustacheTemplateDirectory, mustacheFilename);
        Mustache mustache;
        try (FileReader mustacheFileReader = new FileReader(mustacheFile)) {
            mustache = mustacheFactory.compile(mustacheFileReader, mustacheFilename);
        }
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
        MustacheFactory mustacheFactory = new DefaultMustacheFactory();
        String mustacheFilename = "Module.java.mustache";
        File mustacheFile = new File(mustacheTemplateDirectory, mustacheFilename);
        Mustache mustache;
        try (FileReader mustacheFileReader = new FileReader(mustacheFile)) {
            mustache = mustacheFactory.compile(mustacheFileReader, mustacheFilename);
        }
        File dataElementDir = new File(javaOutputDirectory, "com/agfa/typeddicom/modules");
        //noinspection ResultOfMethodCallIgnored
        dataElementDir.mkdirs();
        for (ModuleMetaInfo moduleMetaInfo : moduleMetaInfoSet) {
            String filename = moduleMetaInfo.getKeyword() + ".java";
            File javaFile = new File(dataElementDir, filename);
            try (FileWriter javaFileWriter = new FileWriter(javaFile)) {
                mustache.execute(javaFileWriter, moduleMetaInfo);
            }
        }
    }


}
