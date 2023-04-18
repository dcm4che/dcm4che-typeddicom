package org.dcm4che.typeddicom.parser;

import org.dcm4che.typeddicom.parser.metamodel.DataElementMetaInfo;
import org.dcm4che.typeddicom.parser.metamodel.ValueRepresentationMetaInfo;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class parses the 6th part of the DICOM Standard XML
 * (http://dicom.nema.org/medical/dicom/current/source/docbook/part06/part06.xml)
 */
public class DicomPart06Handler extends AbstractDicomPartHandler {
    private final Map<String, ValueRepresentationMetaInfo> valueRepresentationsMap;
    private final Set<DataElementMetaInfo> dataElements;
    private boolean inAttributeRegistryTable = false;
    private int currentColumn = 0;
    private DataElementMetaInfo currentDataElementMetaInfo = null;
    private boolean inAttributeRegistryTableBody = false;

    public DicomPart06Handler(Map<String, ValueRepresentationMetaInfo> valueRepresentationsMap) {
        this(valueRepresentationsMap, new HashSet<>());
    }

    public DicomPart06Handler(Map<String, ValueRepresentationMetaInfo> valueRepresentationsMap, Set<DataElementMetaInfo> dataElements) {
        this.valueRepresentationsMap = valueRepresentationsMap;
        this.dataElements = dataElements;
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
                case 1:
                    this.currentDataElementMetaInfo.setTag(getRecordedText());
                    break;
                case 2:
                    this.currentDataElementMetaInfo.setName(getRecordedText());
                    break;
                case 3:
                    this.currentDataElementMetaInfo.setKeyword(getRecordedText().replace("\u200B", "").trim());
                    break;
                case 4:
                    this.currentDataElementMetaInfo.setValueRepresentation(getRecordedText());
                    break;
                case 5:
                    this.currentDataElementMetaInfo.setValueMultiplicity(getRecordedText());
                    break;
                case 6:
                    this.currentDataElementMetaInfo.setComment(getRecordedText());
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + currentColumn);
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

        currentDataElementMetaInfo.setTagConstant(getTagConstant(currentDataElementMetaInfo.getTag()));

        String valueRepresentation = currentDataElementMetaInfo.getValueRepresentation();
        if (!valueRepresentation.equals("See Note")) {
            if (valueRepresentation.contains(" ")) {
                String[] valueRepresentations = valueRepresentation.split(" or ");
                for (String vr : valueRepresentations) {
                    DataElementMetaInfo dataElement = new DataElementMetaInfo(currentDataElementMetaInfo);
                    dataElement.setValueRepresentation(vr);
                    dataElement.setValueRepresentationWrapper(valueRepresentationsMap);
                    dataElement.setKeyword(dataElement.getKeyword() + "As" + vr);
                    this.dataElements.add(dataElement);
                }
            } else {
                currentDataElementMetaInfo.setValueRepresentationWrapper(valueRepresentationsMap);
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

    private String getTagConstant(String tag) {
        return "0x" + tag.replace("(", "").replace(",", "").replace(")", "").replace("x", "0");
    }

}
