package org.dcm4che.typeddicom.parser;

import org.dcm4che.typeddicom.parser.metamodel.ValueRepresentationMetaInfo;
import org.dcm4che.typeddicom.parser.utils.KeywordUtils;
import org.dcm4che3.data.VR;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * This class parses the 5th part of the DICOM Standard XML
 * (https://dicom.nema.org/medical/dicom/current/source/docbook/part05/part05.xml) and extracts the Value Representations
 * from there.
 */
public class DicomPart05Handler extends AbstractDicomPartHandler {
    private final List<String> columns = new LinkedList<>();
    private final Set<ValueRepresentationMetaInfo> valueRepresentations = new HashSet<>();
    private boolean isInVRTable;
    private boolean isInVRTableBody;
    private String rowHref = null;

    public Set<ValueRepresentationMetaInfo> getValueRepresentations() {
        return valueRepresentations;
    }

    @Override
    public String getBaseHrefUrl() {
        return getDicomStandardHtmlUrl() + "/part05.html";
    }

    @Override
    protected String getLabelPrefix() {
        return "DICOM Standard Part 5";
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if ("caption".equals(qName)) {
            startRecordingText();
        } else if (isInVRTable && "tbody".equals(qName)) {
            this.isInVRTableBody = true;
        } else if (isInVRTableBody && "td".equals(qName)) {
            startRecordingHTML();
        } else if (isInVRTableBody && rowHref == null && "para".equals(qName)) {
            rowHref = getUrlFromXmlId(attributes.getValue("xml:id"));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("caption".equals(qName)) {
            String caption = getRecordedText();
            if (caption.equals("DICOM Value Representations")) {
                this.isInVRTable = true;
            }
        } else if ("table".equals(qName)) {
            isInVRTable = false;
            isInVRTableBody = false;
        } else if (isInVRTableBody && "td".equals(qName)) {
            columns.add(getRecordedHTML());
        } else if (isInVRTableBody && "tr".equals(qName)) {
            handleEndOfRow();
            this.rowHref = null;
            this.columns.clear();
        }
        super.endElement(uri, localName, qName);
    }

    private void handleEndOfRow() {
        String tag = columns.get(0).replaceAll("(?s)<p>(.*?)</p>.*?<p>(.*?)</p>", "$1");
        String name = columns.get(0).replaceAll("(?s)<p>(.*?)</p>.*?<p>(.*?)</p>", "$2");
        if (tag.equals("SQ")) {
            return;
        }
        Set<String> dataTypes = new HashSet<>();
        VR vr = VR.valueOf(tag);
        if (vr.isTemporalType()) {
            dataTypes.add("DateDataElement");
        }
        if (isDoubleType(vr)) {
            dataTypes.add("DoubleDataElement");
        }
        if (isFloatType(vr)) {
            dataTypes.add("FloatDataElement");
        }
        if (isIntType(vr)) {
            dataTypes.add("IntDataElement");
        }
        if (isStringType(vr)) {
            dataTypes.add("StringDataElement");
        }
        valueRepresentations.add(new ValueRepresentationMetaInfo(
                tag,
                name,
                KeywordUtils.sanitizeAsJavaIdentifier(name),
                columns.get(1),
                columns.get(2),
                columns.get(3),
                rowHref,
                dataTypes
        ));
    }

    private boolean isStringType(VR vr) {
        return vr == VR.AT || vr.isStringType();
    }

    private boolean isIntType(VR vr) {
        return vr == VR.AT || vr.isIntType();
    }

    private boolean isFloatType(VR vr) {
        try {
            vr.toFloat(new double[]{1.f}, true, 0, 0);
            return true;
        } catch (Exception ignored){
            // ignored
        }
        try {
            vr.toFloat(floatToByteArray(1.f), true, 0, 0);
            return true;
        } catch (Exception ignored) {
            // ignored
        }
        return false;
    }

    private boolean isDoubleType(VR vr) {
        try {
            vr.toDouble(new double[]{1.f}, true, 0, 0);
            return true;
        } catch (Exception ignored) {
            // ignored
        }
        try {
            vr.toDouble(doubleToByteArray(1.f), true, 0, 0);
            return true;
        } catch (Exception ignored) {
            // ignored
        }
        return false;
    }

    private byte[] doubleToByteArray(final double i) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeDouble(i);
        dos.flush();
        return bos.toByteArray();
    }

    private byte[] floatToByteArray(final float i) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeFloat(i);
        dos.flush();
        return bos.toByteArray();
    }
}
