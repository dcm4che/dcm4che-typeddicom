package com.agfa.typeddicom.table;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static com.agfa.typeddicom.utils.KeywordUtils.sanitizeAsJavaIdentifier;

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
    private final String href;
    private final List<TableEntry> tableEntries = new ArrayList<>();

    public ModuleTable(String sectionId, String name, String href) {
        this.sectionId = sectionId;
        this.name = name;
        this.keyword = sanitizeAsJavaIdentifier(name);
        this.href = href;
    }

}
