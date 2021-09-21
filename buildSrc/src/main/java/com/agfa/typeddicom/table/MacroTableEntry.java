package com.agfa.typeddicom.table;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO describe this class
 *
 * @author (. @ agfa.com)
 */
@Data
public class MacroTableEntry implements TableEntry {
    private final String tableId;
    private final String additionalInfo;
    private List<TableEntry> subTableEntries = new ArrayList<>();

    public MacroTableEntry(String tableId) {
        this(tableId, "");
    }

    public MacroTableEntry(String tableId, String additionalInfo) {
        this.tableId = tableId;
        this.additionalInfo = additionalInfo;
    }

    @Override
    public List<TableEntry> getSubTableEntries() {
        return this.subTableEntries;
    }
}
