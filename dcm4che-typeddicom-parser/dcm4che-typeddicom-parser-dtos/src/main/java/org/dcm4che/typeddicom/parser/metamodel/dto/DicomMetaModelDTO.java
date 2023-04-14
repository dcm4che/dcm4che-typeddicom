package org.dcm4che.typeddicom.parser.metamodel.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class DicomMetaModelDTO {
    private final Map<String, ValueRepresentationMetaInfoDTO> valueRepresentations;
    private final Map<String, DataElementMetaInfoDTO> dataElements;
    private final Map<String, ModuleMetaInfoDTO> modules;
    private final Map<String, InformationObjectDefinitionMetaInfoDTO> iods;

    @JsonCreator
    public DicomMetaModelDTO(
            @JsonProperty("valueRepresentations") Map<String, ValueRepresentationMetaInfoDTO> valueRepresentations,
            @JsonProperty("dataElements") Map<String, DataElementMetaInfoDTO> dataElements,
            @JsonProperty("modules") Map<String, ModuleMetaInfoDTO> modules,
            @JsonProperty("iods") Map<String, InformationObjectDefinitionMetaInfoDTO> iods
    ) {
        this.valueRepresentations = valueRepresentations != null ? valueRepresentations : new HashMap<>();
        this.dataElements = dataElements != null ? dataElements : new HashMap<>();
        this.modules = modules != null ? modules : new HashMap<>();
        this.iods = iods != null ? iods : new HashMap<>();
    }

    public DicomMetaModelDTO() {
        this(null, null, null, null);
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
