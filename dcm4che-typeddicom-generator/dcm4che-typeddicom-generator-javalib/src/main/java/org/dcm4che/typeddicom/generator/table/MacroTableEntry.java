package org.dcm4che.typeddicom.generator.table;

import java.util.Objects;

public class MacroTableEntry extends TableEntry {
    private final String tableId;
    private final String additionalInfo;

    public MacroTableEntry(String href, String tableId) {
        this(href, tableId, "");
    }

    public MacroTableEntry(String href, String tableId, String additionalInfo) {
        super(href);
        this.tableId = tableId;
        this.additionalInfo = additionalInfo;
    }

    public String getTableId() {
        return tableId;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MacroTableEntry that = (MacroTableEntry) o;
        return Objects.equals(tableId, that.tableId) && Objects.equals(additionalInfo, that.additionalInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tableId, additionalInfo);
    }
}
