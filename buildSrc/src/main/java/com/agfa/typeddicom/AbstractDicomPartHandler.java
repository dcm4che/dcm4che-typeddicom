package com.agfa.typeddicom;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Collections;
import java.util.Map;

/**
 * TODO describe this class
 *
 * @author Niklas Roth (niklas.roth@agfa.com)
 */
public abstract class AbstractDicomPartHandler extends DefaultHandler {
    private final StringBuilder currentText = new StringBuilder();
    private boolean recordText = false;
    private StringBuilder currentHTML = new StringBuilder();
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
            default -> {
                System.out.println("Unknown text tag: " + qName);
                return false;
            }
        }
        return true;
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
            currentHTML.append(ch, start, length);
        }
    }

    public String xmlIdToLabel(String xmlId) {
        return getLabelPrefix() + " - " + xmlId.replace("sect_", "Section ")
                .replace("table_", "Table ");
    }

    public abstract String getBaseHrefUrl();

    protected abstract String getLabelPrefix();
}
