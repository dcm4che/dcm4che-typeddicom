package com.agfa.typeddicom.table;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AttributeTableEntry implements TableEntry {
    private String name;
    private String tag;
    private String type;
    private String attributeDescription;
    private List<TableEntry> subTableEntries = new ArrayList<>();

    public AttributeTableEntry(String name, String tag, String attributeDescription) {
        this(name, tag, "", attributeDescription);
    }

    public AttributeTableEntry(String name, String tag, String type, String attributeDescription) {
        this.name = name;
        this.type = type;
        this.tag = tag;
        this.attributeDescription = attributeDescription;
    }

    @Override
    public List<TableEntry> getSubTableEntries() {
        return subTableEntries;
    }
}
