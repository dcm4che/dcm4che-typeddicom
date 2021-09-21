package com.agfa.typeddicom;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * TODO describe this class
 *
 * @author Niklas Roth (niklas.roth@agfa.com)
 */
public class AbstractDicomPartHandler extends DefaultHandler {
    private final StringBuilder currentText = new StringBuilder();
    private boolean recordText = false;

    protected void startRecordingText() {
        this.currentText.setLength(0);
        this.recordText = true;
    }

    protected String getRecordedText() {
        this.recordText = false;
        return this.currentText.toString().trim();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (recordText) {
            currentText.append(ch, start, length);
        }
    }
}
