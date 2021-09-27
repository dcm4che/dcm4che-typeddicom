package com.agfa.typeddicom.table;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MacroTable {
    private final String name;
    private final String tableId;
    private final String href;
    private final List<TableEntry> tableEntries = new ArrayList<>();
}
