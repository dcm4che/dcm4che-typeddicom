package com.agfa.typeddicom.table;


import java.util.Objects;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAttributeDescription() {
        return attributeDescription;
    }

    public void setAttributeDescription(String attributeDescription) {
        this.attributeDescription = attributeDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AttributeTableEntry that = (AttributeTableEntry) o;
        return Objects.equals(name, that.name) && Objects.equals(tag, that.tag) && Objects.equals(type, that.type) && Objects.equals(attributeDescription, that.attributeDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, tag, type, attributeDescription);
    }
}
