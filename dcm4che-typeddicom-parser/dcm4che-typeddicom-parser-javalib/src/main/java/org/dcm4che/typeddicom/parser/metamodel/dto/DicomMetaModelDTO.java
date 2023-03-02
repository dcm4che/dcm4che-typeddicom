package org.dcm4che.typeddicom.parser.metamodel.dto;

import com.fasterxml.jackson.annotation.JsonMerge;

import java.util.HashMap;
import java.util.Map;

public record DicomMetaModelDTO(
        @JsonMerge
        Map<String, ValueRepresentationMetaInfoDTO> valueRepresentations,

        @JsonMerge
        Map<String, DataElementMetaInfoDTO> dataElements,

        @JsonMerge
        Map<String, ModuleMetaInfoDTO> modules,

        @JsonMerge
        Map<String, InformationObjectDefinitionMetaInfoDTO> iods
) {
    public DicomMetaModelDTO() {
        this(new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>());
    }

    public void addOrUpdate(String keyword, ValueRepresentationMetaInfoDTO newValue) {
        ValueRepresentationMetaInfoDTO oldValue = valueRepresentations.putIfAbsent(keyword, newValue);
        if (oldValue != null) {
            throw new IllegalStateException("There are multiple ValueRepresentationMetaInfoDTOs with the same keyword: " + keyword);
        }
    }

    public void addOrUpdate(String keyword, DataElementMetaInfoDTO newValue) {
        DataElementMetaInfoDTO oldValue = dataElements.putIfAbsent(keyword, newValue);
        if (oldValue != null) {
            throw new IllegalStateException("There are multiple DataElementMetaInfoDTOs with the same keyword: " + keyword);
        }
    }

    public void addOrUpdate(String keyword, ModuleMetaInfoDTO newValue) {
        ModuleMetaInfoDTO oldValue = modules.putIfAbsent(keyword, newValue);
        if (oldValue != null) {
            throw new IllegalStateException("There are multiple ModuleMetaInfoDTOs with the same keyword: " + keyword);
        }
    }

    public void addOrUpdate(String keyword, InformationObjectDefinitionMetaInfoDTO newValue) {
        InformationObjectDefinitionMetaInfoDTO oldValue = iods.putIfAbsent(keyword, newValue);
        if (oldValue != null) {
            throw new IllegalStateException("There are multiple InformationObjectDefinitionMetaInfoDTOs with the same keyword: " + keyword);
        }
    }
}
