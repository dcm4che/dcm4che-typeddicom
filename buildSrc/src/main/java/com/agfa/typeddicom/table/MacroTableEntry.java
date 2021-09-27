package com.agfa.typeddicom.table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO describe this class
 *
 * @author (. @ agfa.com)
 */
@EqualsAndHashCode(callSuper = true)
@Data
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
}
