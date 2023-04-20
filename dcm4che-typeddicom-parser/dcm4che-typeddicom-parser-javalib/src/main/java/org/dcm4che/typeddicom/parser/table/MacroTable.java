package org.dcm4che.typeddicom.parser.table;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MacroTable {
    private final String name;
    private final String tableId;
    private final String href;
    private final List<TableEntry> tableEntries = new ArrayList<>();

    public MacroTable(String name, String tableId, String href) {
        this.name = name;
        this.tableId = tableId;
        this.href = href;
    }

    public String getName() {
        return name;
    }

    public String getTableId() {
        return tableId;
    }

    public String getHref() {
        return href;
    }

    public List<TableEntry> getTableEntries() {
        return tableEntries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MacroTable that = (MacroTable) o;
        return Objects.equals(name, that.name) && Objects.equals(tableId, that.tableId) && Objects.equals(href, that.href) && Objects.equals(tableEntries, that.tableEntries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, tableId, href, tableEntries);
    }
}
