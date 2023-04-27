package org.dcm4che.typeddicom.parser;

import org.dcm4che.typeddicom.parser.metamodel.InformationObjectDefinitionMetaInfo;
import org.dcm4che.typeddicom.parser.table.Table;
import org.dcm4che.typeddicom.parser.table.TableCell;
import org.xml.sax.SAXException;

import java.util.Set;

public class DicomPart04Handler extends MemorizeTablesDicomPartHandler {
    private static final String STANDARD_SOP_CLASSES_TABLE_CAPTION = "Standard SOP Classes";
    private static final String ZERO_WIDTH_SPACE = "\u200B";
    private final Set<InformationObjectDefinitionMetaInfo> iods;

    @Override
    public String getBaseHrefUrl() {
        return getDicomStandardHtmlUrl() + "/part04.html";
    }

    public DicomPart04Handler(Set<InformationObjectDefinitionMetaInfo> iods) {
        this.iods = iods;
    }

    @Override
    protected String getLabelPrefix() {
        return "DICOM Standard Part 4";
    }

    @Override
    public void endDocument() throws SAXException {
        Table standardSOPClassesTable = this.getTables().stream()
                .filter(table -> STANDARD_SOP_CLASSES_TABLE_CAPTION.equals(table.getCaption()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("There is no Standard SOP Classes Table in the document"));
        for (int row = 0; row < standardSOPClassesTable.getRows(); row++) {
            handleSOPClassesTableRow(standardSOPClassesTable, row);
        }
        super.endDocument();
    }

    private void handleSOPClassesTableRow(Table standardSOPClassesTable, int row) {
        String sopClassName = standardSOPClassesTable.getTableCell(row, "SOP Class Name").getContent();
        sopClassName = cleanHTMLText(sopClassName);
        String sopClassUID = standardSOPClassesTable.getTableCell(row, "SOP Class UID").getContent();
        sopClassUID = cleanHTMLText(sopClassUID);
        final String iodTargetPtr = getIODTargetPointer(standardSOPClassesTable.getTableCell(row, "IOD Specification (defined in )"));
        InformationObjectDefinitionMetaInfo matchingIOD = this.iods.stream()
                .filter(iod -> iodTargetPtr.equals(getParentSectionId(iod)))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("There is no IOD with section id " + iodTargetPtr));
        matchingIOD.addSopClass(sopClassName, sopClassUID);
    }

    private String getIODTargetPointer(TableCell tableCell) {
        String iodTargetPtr = tableCell.getContent();
        iodTargetPtr = iodTargetPtr.replaceAll(".*href=\"" + getDicomStandardHtmlUrl() + "/part03.html#(sect_[^\"]+)\".*", "$1");
        iodTargetPtr = cleanHTMLText(iodTargetPtr);
        return iodTargetPtr;
    }

    private String getParentSectionId(InformationObjectDefinitionMetaInfo iod) {
        return iod.getSectionId().replaceAll("(sect_.*).\\d+", "$1");
    }

    private String cleanHTMLText(String htmlText) {
        return htmlText.replace("\n", "")
                .replaceAll("</?p>", "")
                .replace(ZERO_WIDTH_SPACE, "")
                .trim();
    }
}
