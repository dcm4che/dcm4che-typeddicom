package org.dcm4che.typeddicom.parser.metamodel;

import java.util.Objects;

public final class AdditionalAttributeInfo {
    private final String name;
    private final String type;
    private final String attributeDescription;

    public AdditionalAttributeInfo(String name, String type, String attributeDescription) {
        this.name = name;
        this.type = type;
        this.attributeDescription = attributeDescription;
    }

    public String name() {
        return name;
    }

    public String type() {
        return type;
    }

    public String attributeDescription() {
        return attributeDescription;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (AdditionalAttributeInfo) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.type, that.type) &&
                Objects.equals(this.attributeDescription, that.attributeDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, attributeDescription);
    }

    @Override
    public String toString() {
        return "AdditionalAttributeInfo[" +
                "name=" + name + ", " +
                "type=" + type + ", " +
                "attributeDescription=" + attributeDescription + ']';
    }

}
