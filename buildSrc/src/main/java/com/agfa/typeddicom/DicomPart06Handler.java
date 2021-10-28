package com.agfa.typeddicom;

import com.agfa.typeddicom.metamodel.DataElementMetaInfo;
import com.agfa.typeddicom.metamodel.ValueRepresentationMetaInfo;
import org.dcm4che3.data.Tag;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class parses the 6th part of the DICOM Standard XML
 * (http://dicom.nema.org/medical/dicom/current/source/docbook/part06/part06.xml)
 */
public class DicomPart06Handler extends AbstractDicomPartHandler {
    private final Map<String, ValueRepresentationMetaInfo> valueRepresentationsMap;
    private final Map<String, ValueRepresentationMetaInfo> multiValueRepresentationsMap;
    private final Set<DataElementMetaInfo> dataElements;
    private boolean inAttributeRegistryTable = false;
    private int currentColumn = 0;
    private DataElementMetaInfo currentDataElementMetaInfo = null;
    private boolean inAttributeRegistryTableBody = false;
    private Map<String, Integer> tagConstants;

    public DicomPart06Handler(Map<String, ValueRepresentationMetaInfo> valueRepresentationsMap, Map<String, ValueRepresentationMetaInfo> multiValueRepresentationsMap) {
        this(valueRepresentationsMap, multiValueRepresentationsMap, new HashSet<>());
    }

    public DicomPart06Handler(Map<String, ValueRepresentationMetaInfo> valueRepresentationsMap, Map<String, ValueRepresentationMetaInfo> multiValueRepresentationsMap, Set<DataElementMetaInfo> dataElements) {
        this.valueRepresentationsMap = valueRepresentationsMap;
        this.multiValueRepresentationsMap = multiValueRepresentationsMap;
        this.dataElements = dataElements;
    }


    @Override
    public void startDocument() throws SAXException {
        tagConstants = Arrays.stream(Tag.class.getDeclaredFields())
                .filter(field -> field.getType().equals(int.class))
                .filter(field -> Modifier.isStatic(field.getModifiers()))
                .collect(
                        Collectors.toMap(
                                Field::getName,
                                field -> {
                                    try {
                                        return field.getInt(null);
                                    } catch (IllegalAccessException e) {
                                        throw new RuntimeException("Could not access Tag." + field.getName() + " with reflection.");
                                    }
                                }
                        )
                );
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if ("caption".equals(qName)) {
            startRecordingText();
        } else if (inAttributeRegistryTable && "tbody".equals(qName)) {
            this.inAttributeRegistryTableBody = true;
        } else if (inAttributeRegistryTableBody && "tr".equals(qName)) {
            this.currentDataElementMetaInfo = new DataElementMetaInfo();
            this.currentColumn = 0;
        } else if (currentDataElementMetaInfo != null && "td".equals(qName)) {
            startRecordingText();
            this.currentColumn++;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("caption".equals(qName)) {
            String recordedText = getRecordedText();
            if (recordedText.startsWith("Registry of DICOM ") && !recordedText.equals("Registry of DICOM Unique Identifiers (UIDs) (Normative)")) {
                this.inAttributeRegistryTable = true;
            }
        } else if ("table".equals(qName)) {
            this.inAttributeRegistryTable = false;
        } else if ("tbody".equals(qName)) {
            this.inAttributeRegistryTableBody = false;
        } else if (this.currentDataElementMetaInfo != null && "tr".equals(qName)) {
            endOfDataElement();
        } else if (this.currentDataElementMetaInfo != null && "td".equals(qName)) {
            switch (currentColumn) {
                case 1 -> this.currentDataElementMetaInfo.setTag(getRecordedText());
                case 2 -> this.currentDataElementMetaInfo.setName(getRecordedText());
                case 3 -> this.currentDataElementMetaInfo.setKeyword(getRecordedText().replace("\u200B", "").trim());
                case 4 -> this.currentDataElementMetaInfo.setValueRepresentation(getRecordedText());
                case 5 -> this.currentDataElementMetaInfo.setValueMultiplicity(getRecordedText());
                case 6 -> this.currentDataElementMetaInfo.setComment(getRecordedText());
                default -> throw new IllegalStateException("Unexpected value: " + currentColumn);
            }
        }
        super.endElement(uri, localName, qName);
    }

    private void endOfDataElement() {
        if (this.currentDataElementMetaInfo.getKeyword().length() == 0) {
            this.currentDataElementMetaInfo.setKeyword("UnknownDataElement" +
                    this.currentDataElementMetaInfo.getTag()
                            .replace("(", "")
                            .replace(")", "")
                            .replace(',', '_'));
        }
        if (this.currentDataElementMetaInfo.getValueRepresentation().length() == 0 ||
                this.currentDataElementMetaInfo.getComment().contains("See Note 2")) {
            this.currentDataElementMetaInfo = null;
            return;
        }
        if (this.currentDataElementMetaInfo.getComment().contains("RET")) {
            this.currentDataElementMetaInfo.setRetired(true);
            String retiredSince = this.currentDataElementMetaInfo.getComment()
                    .replace("RET", "")
                    .replace("(", "")
                    .replace(")", "")
                    .trim();
            this.currentDataElementMetaInfo.setRetiredSince(retiredSince);
        }

        currentDataElementMetaInfo.setTagConstant(getTagConstant(currentDataElementMetaInfo.getKeyword(), currentDataElementMetaInfo.getTag()));

        String valueRepresentation = currentDataElementMetaInfo.getValueRepresentation();
        if (!valueRepresentation.equals("See Note")) {
            if (valueRepresentation.contains(" ")) {
                String[] valueRepresentations = valueRepresentation.split(" or ");
                for (String vr : valueRepresentations) {
                    DataElementMetaInfo dataElement = new DataElementMetaInfo(currentDataElementMetaInfo);
                    dataElement.setValueRepresentation(vr);
                    dataElement.setValueRepresentationWrapper(valueRepresentationsMap, multiValueRepresentationsMap);
                    dataElement.setKeyword(dataElement.getKeyword() + "As" + vr);
                    this.dataElements.add(dataElement);
                }
            } else {
                currentDataElementMetaInfo.setValueRepresentationWrapper(valueRepresentationsMap, multiValueRepresentationsMap);
                this.dataElements.add(this.currentDataElementMetaInfo);
            }
        }
        this.currentDataElementMetaInfo = null;
    }

    public Set<DataElementMetaInfo> getDataElements() {
        return dataElements;
    }

    @Override
    public String getBaseHrefUrl() {
        return "http://dicom.nema.org/medical/dicom/current/output/html/part03.html";
    }

    @Override
    protected String getLabelPrefix() {
        return "DICOM Standard Part 6";
    }

    private String getTagConstant(String keyword, String tag) {
        String tagHexRegex = tag.replace("x,", "[02468ACE],").replace("x", "[0-9A-F]");
        tagHexRegex = tagHexRegex.replace("(", "").replace(",", "").replace(")", "");
        tagHexRegex = tagHexRegex.toLowerCase();
        Integer tagInt = tagConstants.get(keyword);

        String constant;
        if (tagInt != null) {
            String tagHex = Integer.toString(tagInt, 16);
            tagHex = "0".repeat(8 - tagHex.length()) + tagHex;
            if (tagHex.matches(tagHexRegex)) {
                constant = "Tag." + keyword;
            } else {
                constant = "0x" + tagHex;
            }
        } else {
            String tagHex = tag.replace("(", "").replace(",", "").replace(")", "").replace("x", "0");
            constant = "0x" + tagHex;
        }
        return constant;
    }

}
