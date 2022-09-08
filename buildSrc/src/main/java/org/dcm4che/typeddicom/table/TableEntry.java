package org.dcm4che.typeddicom.table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class TableEntry implements Serializable {
    private final String href;
    private final List<TableEntry> subTableEntries = new ArrayList<>();

    public TableEntry(String href) {
        this.href = href;
    }

    public List<TableEntry> getSubTableEntries() {
        return this.subTableEntries;
    }

    public String getHref() {
        return href;
    }
}
