package org.dcm4che.typeddicom.parser.metamodel.dto;

import com.fasterxml.jackson.annotation.JsonMerge;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class DataElementMetaInfoDTO {
    private final String name;
    private final String privateCreatorConstant;
    private final String tag;
    private final String tagConstant;
    private final String valueRepresentation;
    private final String valueMultiplicity;
    private final String comment;
    private final boolean retired;
    private final String retiredSince;
    @JsonMerge
    private final List<AdditionalAttributeInfoContextsDTO> additionalAttributeInfo;
    @JsonMerge
    private final List<String> contains;

    public DataElementMetaInfoDTO(
            @JsonProperty("name") String name,
            @JsonProperty("privateCreatorConstant") String privateCreatorConstant,
            @JsonProperty("tag") String tag,
            @JsonProperty("tagConstant") String tagConstant,
            @JsonProperty("valueRepresentation") String valueRepresentation,
            @JsonProperty("valueMultiplicity") String valueMultiplicity,
            @JsonProperty("comment") String comment,
            @JsonProperty("retired") boolean retired,
            @JsonProperty("retiredSince") String retiredSince,
            @JsonProperty("additionalAttributeInfo") List<AdditionalAttributeInfoContextsDTO> additionalAttributeInfo,
            @JsonProperty("contains") List<String> contains
    ) {
        this.name = name;
        this.privateCreatorConstant = privateCreatorConstant;
        this.tag = tag;
        this.tagConstant = tagConstant;
        this.valueRepresentation = valueRepresentation;
        this.valueMultiplicity = valueMultiplicity;
        this.comment = comment;
        this.retired = retired;
        this.retiredSince = retiredSince;
        this.additionalAttributeInfo = additionalAttributeInfo != null ? additionalAttributeInfo : new ArrayList<>();
        this.contains = contains != null ? contains : new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getPrivateCreatorConstant() {
        return privateCreatorConstant;
    }

    public String getTag() {
        return tag;
    }

    public String getTagConstant() {
        return tagConstant;
    }

    public String getValueRepresentation() {
        return valueRepresentation;
    }

    public String getValueMultiplicity() {
        return valueMultiplicity;
    }

    public String getComment() {
        return comment;
    }

    public boolean getRetired() {
        return retired;
    }

    public String getRetiredSince() {
        return retiredSince;
    }

    public List<AdditionalAttributeInfoContextsDTO> getAdditionalAttributeInfo() {
        return additionalAttributeInfo;
    }

    public List<String> getContains() {
        return contains;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (DataElementMetaInfoDTO) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.privateCreatorConstant, that.privateCreatorConstant) &&
                Objects.equals(this.tag, that.tag) &&
                Objects.equals(this.tagConstant, that.tagConstant) &&
                Objects.equals(this.valueRepresentation, that.valueRepresentation) &&
                Objects.equals(this.valueMultiplicity, that.valueMultiplicity) &&
                Objects.equals(this.comment, that.comment) &&
                this.retired == that.retired &&
                Objects.equals(this.retiredSince, that.retiredSince) &&
                Objects.equals(this.additionalAttributeInfo, that.additionalAttributeInfo) &&
                Objects.equals(this.contains, that.contains);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, privateCreatorConstant, tag, tagConstant, valueRepresentation, valueMultiplicity, comment, retired, retiredSince, additionalAttributeInfo, contains);
    }

    @Override
    public String toString() {
        return "DataElementMetaInfoDTO[" +
                "name=" + name + ", " +
                "privateCreatorConstant=" + privateCreatorConstant + ", " +
                "tag=" + tag + ", " +
                "tagConstant=" + tagConstant + ", " +
                "valueRepresentation=" + valueRepresentation + ", " +
                "valueMultiplicity=" + valueMultiplicity + ", " +
                "comment=" + comment + ", " +
                "retired=" + retired + ", " +
                "retiredSince=" + retiredSince + ", " +
                "additionalAttributeInfo=" + additionalAttributeInfo + ", " +
                "contains=" + contains + ']';
    }
}
