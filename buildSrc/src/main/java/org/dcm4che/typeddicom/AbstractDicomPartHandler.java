package org.dcm4che.typeddicom;

import org.apache.commons.text.StringEscapeUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class records HTML or Text after calling the start methods and stops after calling the corresponding getters.
 */
public abstract class AbstractDicomPartHandler extends DefaultHandler {
    protected static final String DICOM_STANDARD_HTML_URL = "http://dicom.nema.org/medical/dicom/current/output/html";
    private final StringBuilder currentText = new StringBuilder();
    private final Pattern partNumberPattern = Pattern.compile("PS3\\.(?<part>\\d+)");
    private final StringBuilder currentHTML = new StringBuilder();
    private boolean recordText = false;
    private boolean recordHTML;
    private boolean inVariableList;

    protected void startRecordingText() {
        this.currentText.setLength(0);
        this.recordText = true;
    }

    protected String getRecordedText() {
        this.recordText = false;
        return this.currentText.toString().trim().replaceAll(" +", " ");
    }

    protected void startRecordingHTML() {
        this.currentHTML.setLength(0);
        this.recordHTML = true;
    }

    protected String getRecordedHTML() {
        this.recordHTML = false;
        return this.currentHTML.toString().trim().replaceAll(" +", " ");
    }


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (this.recordHTML) {
            boolean handled = convertToAndAppendHTML(qName, attributes, false);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (this.recordHTML) {
            boolean handled = convertToAndAppendHTML(qName, null, true);
        }
    }

    private boolean convertToAndAppendHTML(String qName, Attributes attributes, boolean close) {
        switch (qName) {
            case "para" -> appendTag("p", close);
            case "itemizedlist" -> appendTag("ul", close);
            case "emphasis" -> appendTag("em", close);
            case "superscript" -> appendTag("sup", close);
            case "subscript" -> appendTag("sub", close);
            case "orderedlist" -> appendTag("ol", close);
            case "variablelist" -> {
                appendTag("dl", close);
                inVariableList = !close;
            }
            case "title" -> {
                if (!close) {
                    appendTag("p", false);
                    appendTag("strong", false);
                } else {
                    appendTag("strong", true);
                    appendTag("p", true);
                }
            }
            case "term" -> appendTag("dt", close);
            case "listitem" -> {
                if (inVariableList) {
                    appendTag("dd", close);
                } else {
                    appendTag("li", close);
                }
            }
            case "xref" -> {
                if (!close) {
                    String xmlId = attributes.getValue("linkend");
                    appendTag(
                            "a",
                            false,
                            Collections.singletonMap("href", getUrlFromXmlId(xmlId)),
                            xmlIdToLabel(xmlId)
                    );
                } else {
                    appendTag("a", true);
                }
            }
            case "varlistentry" -> {
                return true;
            }
            case "note" -> {
                if (!close) {
                    appendTag("div", false, Map.of("class", "note", "style", "font-style: italic; margin-left: 0.5in;"), "");
                    appendTag("strong", false, Collections.emptyMap(), "Note");
                    appendTag("strong", true);
                } else {
                    appendTag("div", true);
                }
            }
            case "olink" -> {
                if (!close) {
                    String targetptr = attributes.getValue("targetptr");
                    String targetdoc = attributes.getValue("targetdoc");
                    appendTag(
                            "a",
                            false,
                            Collections.singletonMap("href", getUrlFromTarget(targetdoc, targetptr)),
                            targetptr.replace("sect_", "").replace("_", "")
                    );
                } else {
                    appendTag("a", true);
                }
            }
            case "link" -> {
                if (!close) {
                    appendTag(
                            "a",
                            false,
                            Collections.singletonMap("href", attributes.getValue("xl:href")),
                            attributes.getValue("xl:href")
                    );
                } else {
                    appendTag("a", true);
                }
            }
            default -> {
                System.out.println("Unknown text tag: " + qName);
                return false;
            }
        }
        return true;
    }

    private String getUrlFromTarget(String targetdoc, String targetptr) {
        Integer partNumber = null;
        Matcher matcher = partNumberPattern.matcher(targetdoc);
        if (matcher.find()) {
            partNumber = Integer.parseInt(matcher.group("part"));
            return String.format("%s/part%02d.html#%s", DICOM_STANDARD_HTML_URL, partNumber, targetptr);
        }else {
            throw new IllegalArgumentException("Invalid targetdoc " + targetptr + ". " +
                    "Needs to be of form 'PS3\\.(\\d+)'.");
        }
    }

    public String getUrlFromXmlId(String xmlId) {
        return this.getBaseHrefUrl() + "#" + xmlId;
    }

    private void appendTag(String tag, boolean close) {
        appendTag(tag, close, null, "");
    }

    private void appendTag(String tag, boolean close, Map<String, String> attr, String innerHTML) {
        this.currentHTML.append('<');
        if (close) {
            this.currentHTML.append('/');
        }
        this.currentHTML.append(tag);
        if (attr != null) {
            for (Map.Entry<String, String> attrEntry : attr.entrySet()) {
                this.currentHTML.append(' ');
                this.currentHTML.append(attrEntry.getKey());
                this.currentHTML.append("=\"");
                this.currentHTML.append(attrEntry.getValue());
                this.currentHTML.append('\"');
            }
        }
        this.currentHTML.append('>');
        this.currentHTML.append(innerHTML);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (recordText) {
            currentText.append(ch, start, length);
        }
        if (recordHTML) {
            currentHTML.append(StringEscapeUtils.escapeHtml4(new String(ch, start, length)));
        }
    }

    public String xmlIdToLabel(String xmlId) {
        return getLabelPrefix() + " - " + xmlId.replace("sect_", "Section ")
                .replace("table_", "Table ");
    }

    public abstract String getBaseHrefUrl();

    protected abstract String getLabelPrefix();
}
