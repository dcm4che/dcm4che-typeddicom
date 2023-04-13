package org.dcm4che.typeddicom.parser.metamodel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public final class AdditionalAttributeInfoDTO {
    private final String name;
    private final String type;
    private final String attributeDescription;

    public AdditionalAttributeInfoDTO(
            @JsonProperty("name") String name,
            @JsonProperty("type") String type,
            @JsonProperty("attributeDescription") String attributeDescription
    ) {
        this.name = name;
        this.type = type;
        this.attributeDescription = attributeDescription;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getAttributeDescription() {
        return attributeDescription;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (AdditionalAttributeInfoDTO) obj;
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
        return "AdditionalAttributeInfoDTO[" +
                "name=" + name + ", " +
                "type=" + type + ", " +
                "attributeDescription=" + attributeDescription + ']';
    }

}
