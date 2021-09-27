package com.agfa.typeddicom.table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO describe this class
 *
 * @author (. @ agfa.com)
 */
@Data
@NoArgsConstructor
public abstract class TableEntry implements Serializable {
    private String href;
    private final List<TableEntry> subTableEntries = new ArrayList<>();

    public TableEntry(String href) {
        this.href = href;
    }

    public List<TableEntry> getSubTableEntries() {
        return this.subTableEntries;
    }
}
