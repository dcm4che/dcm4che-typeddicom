package com.agfa.typeddicom;

import com.agfa.typeddicom.table.Table;
import com.agfa.typeddicom.table.TableCell;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.List;

/**
 * This abstract class is used to parse tables contained in the XML doc. The tables can be retrieved with
 * {@link #getTables()}.
 */
public abstract class MemorizeTablesDicomPartHandler extends AbstractDicomPartHandler {
    private final List<Table> tables = new ArrayList<>();
    private Table currentTable = null;
    private int currentRow;
    private boolean inBody;
    private TableCell currentCell;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if ("table".equals(qName)) {
            String xmlId = attributes.getValue("xml:id");
            currentTable = new Table(xmlId, getUrlFromXmlId(xmlId));
        } else if (currentTable != null && "caption".equals(qName)) {
            startRecordingText();
        } else if (currentTable != null && "th".equals(qName)) {
            startRecordingText();
        } else if (currentTable != null && "tbody".equals(qName)) {
            inBody = true;
            currentRow = 0;
        } else if (inBody && "td".equals(qName)) {
            String colspan = attributes.getValue("colspan");
            String rowspan = attributes.getValue("rowspan");
            currentCell = new TableCell(
                    Integer.parseInt(colspan==null?"1":colspan),
                    Integer.parseInt(rowspan==null?"1":rowspan)
            );
            startRecordingHTML();
        }
    }
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (currentTable != null && "table".equals(qName)) {
            this.tables.add(currentTable);
            currentTable = null;
        } else if (currentTable != null && "caption".equals(qName)) {
            currentTable.setCaption(getRecordedText());
        } else if (currentTable != null && "th".equals(qName)) {
            currentTable.getColumnHeaders().add(getRecordedText());
        } else if (currentTable != null && "tbody".equals(qName)) {
            inBody = false;
        } else if (inBody && "tr".equals(qName)) {
            currentRow++;
        } else if (inBody && "td".equals(qName)) {
            currentCell.setContent(getRecordedHTML());
            currentTable.addTableCell(currentRow, currentCell);
        }
        super.endElement(uri, localName, qName);
    }

    public List<Table> getTables() {
        return tables;
    }
}
