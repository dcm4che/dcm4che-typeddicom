package com.agfa.typeddicom.table;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static com.agfa.typeddicom.KeywordUtils.sanitizeAsJavaIdentifier;

/**
 * TODO describe this class
 *
 * @author (. @ agfa.com)
 */
@Data
public class ModuleTable {
    private final String sectionId;
    private final String name;
    private final String keyword;
    private final List<TableEntry> tableEntries = new ArrayList<>();

    public ModuleTable(String sectionId, String name) {
        this.sectionId = sectionId;
        this.name = name;
        this.keyword = sanitizeAsJavaIdentifier(name);
    }

}
