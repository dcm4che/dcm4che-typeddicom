package com.agfa.typeddicom.table;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MacroTable {
    private final String tableId;
    private final List<TableEntry> tableEntries = new ArrayList<>();
}
