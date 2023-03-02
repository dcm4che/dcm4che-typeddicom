package org.dcm4che.typeddicom.parser.metamodel.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.stream.Collectors;

public record DataElementMetaInfoDTO(
        String name,
        String tag,
        String tagConstant,
        String valueRepresentation,
        String valueMultiplicity,
        String comment,
        boolean retired,
        String retiredSince,
        List<AdditionalAttributeInfoContextsDTO> additionalAttributeInfo,
        List<String> contains
) {

    @JsonIgnore
    public boolean isSequence() {
        return valueRepresentation.equals("Sequence");
    }

    @JsonIgnore
    public String getValueRepresentationWrapper() {
        if (valueMultiplicity().equals("1")) {
            return valueRepresentation + "Wrapper";
        } else {
            return valueRepresentation + "MultiWrapper";
        }
    }

    @JsonIgnore
    public String implementsBuilderInterfaces() {
        return contains.stream()
                .map(key -> key + ".Builder<Builder, Item>")
                .collect(Collectors.joining(", "));
    }

    @JsonIgnore
    public String implementsHolderInterfaces() {
        return contains.stream().map(key -> key + ".Holder").collect(Collectors.joining(", "));
    }
}
