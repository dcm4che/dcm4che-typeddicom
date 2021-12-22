package org.dcm4che.typeddicom.table;


import java.util.Objects;

/**
 * TODO describe this class
 *
 * @author (. @ agfa.com)
 */
public class TableCell {
    private final int colSpan;
    private final int rowSpan;
    private String content;

    public TableCell(int colSpan, int rowSpan) {
        this.colSpan = colSpan;
        this.rowSpan = rowSpan;
    }

    public int getColSpan() {
        return colSpan;
    }

    public int getRowSpan() {
        return rowSpan;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableCell tableCell = (TableCell) o;
        return colSpan == tableCell.colSpan && rowSpan == tableCell.rowSpan && Objects.equals(content, tableCell.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(colSpan, rowSpan, content);
    }
}
