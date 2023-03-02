package org.dcm4che.typeddicom.parser.metamodel.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.stream.Collectors;

public record ModuleMetaInfoDTO(
        String name,
        String sectionId,
        String href,
        List<String> contains
) {
    @JsonIgnore
    public String implementsBuilderInterfaces() {
        return contains.stream().map(key -> key + ".Builder<SELF, T>").collect(Collectors.joining(", "));
    }

    @JsonIgnore
    public String implementsHolderInterfaces() {
        return contains.stream().map(key -> key + ".Holder").collect(Collectors.joining(", "));
    }
}
