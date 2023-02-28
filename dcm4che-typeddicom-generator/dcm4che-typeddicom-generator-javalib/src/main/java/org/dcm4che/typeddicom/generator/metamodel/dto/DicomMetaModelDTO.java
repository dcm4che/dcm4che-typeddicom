package org.dcm4che.typeddicom.generator.metamodel.dto;

import org.dcm4che.typeddicom.generator.utils.RecordUtils;

import java.util.HashMap;
import java.util.Map;

public record DicomMetaModelDTO(
        Map<String, ValueRepresentationMetaInfoDTO> valueRepresentations,
        Map<String, DataElementMetaInfoDTO> dataElements,
        Map<String, ModuleMetaInfoDTO> modules,
        Map<String, InformationObjectDefinitionMetaInfoDTO> iods
) {
    public DicomMetaModelDTO() {
        this(new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>());
    }

    public void addOrUpdate(String keyword, ValueRepresentationMetaInfoDTO newValue) {
        ValueRepresentationMetaInfoDTO oldValue = valueRepresentations.putIfAbsent(keyword, newValue);
        if (oldValue != null) {
            newValue = RecordUtils.combineRecords(oldValue, newValue, ValueRepresentationMetaInfoDTO.class);
            valueRepresentations.put(keyword, newValue);
        }
    }

    public void addOrUpdate(String keyword, DataElementMetaInfoDTO newValue) {
        DataElementMetaInfoDTO oldValue = dataElements.putIfAbsent(keyword, newValue);
        if (oldValue != null) {
            newValue = RecordUtils.combineRecords(oldValue, newValue, DataElementMetaInfoDTO.class);
            dataElements.put(keyword, newValue);
        }
    }

    public void addOrUpdate(String keyword, ModuleMetaInfoDTO newValue) {
        ModuleMetaInfoDTO oldValue = modules.putIfAbsent(keyword, newValue);
        if (oldValue != null) {
            newValue = RecordUtils.combineRecords(oldValue, newValue, ModuleMetaInfoDTO.class);
            modules.put(keyword, newValue);
        }
    }

    public void addOrUpdate(String keyword, InformationObjectDefinitionMetaInfoDTO newValue) {
        InformationObjectDefinitionMetaInfoDTO oldValue = iods.putIfAbsent(keyword, newValue);
        if (oldValue != null) {
            newValue = RecordUtils.combineRecords(oldValue, newValue, InformationObjectDefinitionMetaInfoDTO.class);
            iods.put(keyword, newValue);
        }
    }
}
