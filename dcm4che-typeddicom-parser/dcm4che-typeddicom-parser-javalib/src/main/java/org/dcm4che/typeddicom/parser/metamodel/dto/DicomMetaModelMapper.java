package org.dcm4che.typeddicom.parser.metamodel.dto;

import org.dcm4che.typeddicom.parser.metamodel.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.StreamSupport;

public class DicomMetaModelMapper {
    public DicomMetaModelDTO mapDicomMetaModelToDicomMetaModelDTO(DicomMetaModel dicomMetaModel) {
        DicomMetaModelDTO dicomMetaModelDTO = new DicomMetaModelDTO();
        for (ValueRepresentationMetaInfo valueRepresentation : dicomMetaModel.getValueRepresentations()) {
            dicomMetaModelDTO.addOrUpdate(valueRepresentation.keyword(), mapValueRepresentationMetaInfoToValueRepresentationMetaInfoDTO(valueRepresentation));
        }
        for (DataElementMetaInfo dataElementMetaInfo : dicomMetaModel.getDataElements()) {
            dicomMetaModelDTO.addOrUpdate(dataElementMetaInfo.getKeyword(), mapDataElementMetaInfoToDataElementMetaInfoDTO(dataElementMetaInfo));
        }
        for (ModuleMetaInfo moduleMetaInfo : dicomMetaModel.getModules()) {
            dicomMetaModelDTO.addOrUpdate(moduleMetaInfo.getKeyword(), mapModuleMetaInfoToModuleMetaInfoDTO(moduleMetaInfo));
        }
        for (InformationObjectDefinitionMetaInfo informationObjectDefinitionMetaInfo : dicomMetaModel.getIods()) {
            dicomMetaModelDTO.addOrUpdate(informationObjectDefinitionMetaInfo.getKeyword(), mapInformationObjectDefinitionMetaInfoToInformationObjectDefinitionMetaInfoDTO(informationObjectDefinitionMetaInfo));
        }
        return dicomMetaModelDTO;
    }

    private ValueRepresentationMetaInfoDTO mapValueRepresentationMetaInfoToValueRepresentationMetaInfoDTO(ValueRepresentationMetaInfo valueRepresentationMetaInfo) {
        return new ValueRepresentationMetaInfoDTO(
                valueRepresentationMetaInfo.name(),
                valueRepresentationMetaInfo.tag(),
                valueRepresentationMetaInfo.definition(),
                valueRepresentationMetaInfo.characterRepertoire(),
                valueRepresentationMetaInfo.lengthOfValue(),
                valueRepresentationMetaInfo.href(),
                valueRepresentationMetaInfo.dataTypes().stream().toList()
        );
    }

    private DataElementMetaInfoDTO mapDataElementMetaInfoToDataElementMetaInfoDTO(DataElementMetaInfo dataElementMetaInfo) {
        return new DataElementMetaInfoDTO(
                dataElementMetaInfo.getName(),
                dataElementMetaInfo.getTag(),
                dataElementMetaInfo.getTagConstant(),
                dataElementMetaInfo.getValueRepresentationWrapper(),
                dataElementMetaInfo.getValueMultiplicity(),
                dataElementMetaInfo.getComment(),
                dataElementMetaInfo.isRetired(),
                dataElementMetaInfo.getRetiredSince(),
                dataElementMetaInfo.getContextsOfAdditionalAttributeInfo().entrySet().stream()
                        .map(this::mapAdditionalAttributeInfoContextsEntryToAdditionalAttributeInfoContextsDTO)
                        .toList(),
                StreamSupport.stream(dataElementMetaInfo.getSubDataElementMetaInfos().spliterator(), false)
                        .map(DataElementMetaInfo::getKeyword)
                        .toList()
        );
    }

    private AdditionalAttributeInfoContextsDTO mapAdditionalAttributeInfoContextsEntryToAdditionalAttributeInfoContextsDTO(Map.Entry<AdditionalAttributeInfo, Set<Context>> additionalAttributeInfoContextsEntry) {
        return new AdditionalAttributeInfoContextsDTO(
                mapAdditionalAttributeInfoToAdditionalAttributeInfoDTO(additionalAttributeInfoContextsEntry.getKey()),
                additionalAttributeInfoContextsEntry.getValue().stream()
                        .map(this::mapContextToContextEntryDTOs)
                        .toList()
        );
    }

    private List<ContextEntryDTO> mapContextToContextEntryDTOs(Context context) {
        return context.getContext().stream().map(this::mapContextEntryToContextEntryDTO).toList();
    }

    private ContextEntryDTO mapContextEntryToContextEntryDTO(ContextEntry contextEntry) {
        return new ContextEntryDTO(contextEntry.contextName(), contextEntry.href());
    }

    private static AdditionalAttributeInfoDTO mapAdditionalAttributeInfoToAdditionalAttributeInfoDTO(AdditionalAttributeInfo additionalAttributeInfo) {
        return new AdditionalAttributeInfoDTO(additionalAttributeInfo.name(), additionalAttributeInfo.type(), additionalAttributeInfo.attributeDescription());
    }

    private ModuleMetaInfoDTO mapModuleMetaInfoToModuleMetaInfoDTO(ModuleMetaInfo moduleMetaInfo) {
        return new ModuleMetaInfoDTO(
                moduleMetaInfo.getName(),
                moduleMetaInfo.getSectionId(),
                moduleMetaInfo.getHref(),
                StreamSupport.stream(moduleMetaInfo.getSubDataElementMetaInfos().spliterator(), false)
                        .map(DataElementMetaInfo::getKeyword).toList()
        );
    }

    private InformationObjectDefinitionMetaInfoDTO mapInformationObjectDefinitionMetaInfoToInformationObjectDefinitionMetaInfoDTO(InformationObjectDefinitionMetaInfo informationObjectDefinitionMetaInfo) {
        return new InformationObjectDefinitionMetaInfoDTO(
                informationObjectDefinitionMetaInfo.getName(),
                informationObjectDefinitionMetaInfo.getHref(),
                informationObjectDefinitionMetaInfo.getSectionId(),
                informationObjectDefinitionMetaInfo.getSopClasses().stream()
                        .map(this::mapSopClassToSopClassDTO)
                        .toList(),
                informationObjectDefinitionMetaInfo.getModuleReferences().stream()
                        .map(ref -> ref.moduleMetaInfo().getKeyword())
                        .toList()
        );
    }

    private SOPClassDTO mapSopClassToSopClassDTO(SOPClass sopClass) {
        return new SOPClassDTO(sopClass.name(), sopClass.uid());
    }
}
