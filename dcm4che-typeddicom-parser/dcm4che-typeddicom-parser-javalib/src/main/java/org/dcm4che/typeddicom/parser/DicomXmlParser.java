package org.dcm4che.typeddicom.parser;

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.dcm4che.typeddicom.parser.metamodel.DataElementMetaInfo;
import org.dcm4che.typeddicom.parser.metamodel.DicomMetaModel;
import org.dcm4che.typeddicom.parser.metamodel.InformationObjectDefinitionMetaInfo;
import org.dcm4che.typeddicom.parser.metamodel.ValueRepresentationMetaInfo;
import org.dcm4che.typeddicom.parser.metamodel.dto.DicomMetaModelDTO;
import org.dcm4che.typeddicom.parser.metamodel.dto.DicomMetaModelMapper;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DicomXmlParser {
    private final SAXParser saxParser;
    private final File dicomXmlDirectory;
    private final File yamlOutputDirectory;

    public DicomXmlParser(File dicomXmlDirectory, File yamlOutputDirectory) {
        try {
            this.dicomXmlDirectory = dicomXmlDirectory;
            this.yamlOutputDirectory = yamlOutputDirectory;
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            saxParser = saxParserFactory.newSAXParser();
        } catch (ParserConfigurationException | SAXException e) {
            throw new RuntimeException("Wasn't able to create the DICOM XML Parser", e);
        }
    }

    public void generateYamlMetamodel() {
        try {
            DicomMetaModel dicomMetaModel = new DicomMetaModel();
            DicomPart05Handler handlerPart5 = new DicomPart05Handler();
            saxParser.parse(new File(dicomXmlDirectory, "part05.xml"), handlerPart5);
            Set<ValueRepresentationMetaInfo> valueRepresentations = handlerPart5.getValueRepresentations();

            dicomMetaModel.addValueRepresentations(valueRepresentations);

            Map<String, ValueRepresentationMetaInfo> valueRepresentationsMap =
                    valueRepresentations.stream().collect(Collectors.toMap(
                            ValueRepresentationMetaInfo::tag,
                            Function.identity())
                    );

            DicomPart06Handler handlerPart6 = new DicomPart06Handler(valueRepresentationsMap);
            saxParser.parse(new File(dicomXmlDirectory, "part06.xml"), handlerPart6);
            Set<DataElementMetaInfo> dataElements = handlerPart6.getDataElements();
            dicomMetaModel.addDataElements(dataElements);

            Map<String, Set<DataElementMetaInfo>> dataElementMetaInfoMap = new HashMap<>();
            for (DataElementMetaInfo dataElement : dataElements) {
                dataElementMetaInfoMap.computeIfAbsent(dataElement.getTag(), (key) -> new HashSet<>());
                dataElementMetaInfoMap.get(dataElement.getTag()).add(dataElement);
            }

            DicomPart03Handler handlerPart3 = new DicomPart03Handler(dataElementMetaInfoMap);
            saxParser.parse(new File(dicomXmlDirectory, "part03.xml"), handlerPart3);
            dicomMetaModel.addModules(handlerPart3.getModules());
            Set<InformationObjectDefinitionMetaInfo> iods = handlerPart3.getIODs();
            dicomMetaModel.addIods(iods);
            
            DicomPart04Handler handlerPart4 = new DicomPart04Handler(iods);
            saxParser.parse(new File(dicomXmlDirectory, "part04.xml"), handlerPart4);

            generateYamlFile(dicomMetaModel);
        } catch (SAXException | IOException e) {
            throw new RuntimeException("Wasn't able to generate the Sources", e);
        }
    }

    private void generateYamlFile(DicomMetaModel dicomMetaModel) {
        DicomMetaModelMapper dtoMapper = new DicomMetaModelMapper();
        DicomMetaModelDTO dicomMetaModelDTO = dtoMapper.mapDicomMetaModelToDicomMetaModelDTO(dicomMetaModel);

        YAMLMapper yamlMapper = new YAMLMapper(new YAMLFactory());
        yamlMapper.configure(YAMLGenerator.Feature.WRITE_DOC_START_MARKER, false);
        yamlMapper.configure(YAMLGenerator.Feature.SPLIT_LINES, false);
        yamlMapper.configure(YAMLGenerator.Feature.LITERAL_BLOCK_STYLE, true);
        yamlMapper.configure(YAMLGenerator.Feature.MINIMIZE_QUOTES, true);
        try {
            yamlMapper.writeValue(new File(yamlOutputDirectory, "std.dicom-meta-model.yaml"), dicomMetaModelDTO);
        } catch (IOException e) {
            throw new RuntimeException("Wasn't able to generate the YAML file", e);
        }
    }
}
