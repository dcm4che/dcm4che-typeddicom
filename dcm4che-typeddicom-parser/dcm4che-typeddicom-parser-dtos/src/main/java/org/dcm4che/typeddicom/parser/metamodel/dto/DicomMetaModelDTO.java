package org.dcm4che.typeddicom.parser.metamodel.dto;

import com.fasterxml.jackson.annotation.JsonMerge;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class DicomMetaModelDTO {
    @JsonMerge
    private final Map<String, ValueRepresentationMetaInfoDTO> valueRepresentations;
    @JsonMerge
    private final Map<String, DataElementMetaInfoDTO> dataElements;
    @JsonMerge
    private final Map<String, ModuleMetaInfoDTO> modules;
    @JsonMerge
    private final Map<String, InformationObjectDefinitionMetaInfoDTO> iods;

    public DicomMetaModelDTO(
            @JsonProperty Map<String, ValueRepresentationMetaInfoDTO> valueRepresentations,
            @JsonProperty Map<String, DataElementMetaInfoDTO> dataElements,
            @JsonProperty Map<String, ModuleMetaInfoDTO> modules,
            @JsonProperty Map<String, InformationObjectDefinitionMetaInfoDTO> iods
    ) {
        this.valueRepresentations = valueRepresentations;
        this.dataElements = dataElements;
        this.modules = modules;
        this.iods = iods;
    }

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

    public Map<String, ValueRepresentationMetaInfoDTO> getValueRepresentations() {
        return valueRepresentations;
    }

    public Map<String, DataElementMetaInfoDTO> getDataElements() {
        return dataElements;
    }

    public Map<String, ModuleMetaInfoDTO> getModules() {
        return modules;
    }

    public Map<String, InformationObjectDefinitionMetaInfoDTO> getIods() {
        return iods;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (DicomMetaModelDTO) obj;
        return Objects.equals(this.valueRepresentations, that.valueRepresentations) &&
                Objects.equals(this.dataElements, that.dataElements) &&
                Objects.equals(this.modules, that.modules) &&
                Objects.equals(this.iods, that.iods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valueRepresentations, dataElements, modules, iods);
    }

    @Override
    public String toString() {
        return "DicomMetaModelDTO[" +
                "valueRepresentations=" + valueRepresentations + ", " +
                "dataElements=" + dataElements + ", " +
                "modules=" + modules + ", " +
                "iods=" + iods + ']';
    }

}
