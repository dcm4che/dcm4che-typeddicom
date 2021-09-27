package com.agfa.typeddicom.table;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class AttributeTableEntry extends TableEntry {
    private String name;
    private String tag;
    private String type;
    private String attributeDescription;

    public AttributeTableEntry(String href, String name, String tag, String attributeDescription) {
        this(href, name, tag, "", attributeDescription);
    }

    public AttributeTableEntry(String href, String name, String tag, String type, String attributeDescription) {
        super(href);
        this.name = name;
        this.type = type;
        this.tag = tag;
        this.attributeDescription = attributeDescription;
    }
}
