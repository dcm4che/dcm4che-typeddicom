package org.dcm4che.typeddicom.parser.table;


import java.util.ArrayList;
import java.util.List;

import static org.dcm4che.typeddicom.parser.utils.KeywordUtils.sanitizeAsJavaIdentifier;

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

    public String getSectionId() {
        return sectionId;
    }

    public String getName() {
        return name;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getHref() {
        return href;
    }

    public List<TableEntry> getTableEntries() {
        return tableEntries;
    }
}
