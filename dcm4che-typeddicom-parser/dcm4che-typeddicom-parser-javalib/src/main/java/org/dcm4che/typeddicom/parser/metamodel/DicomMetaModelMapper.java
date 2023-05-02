package org.dcm4che.typeddicom.parser.metamodel;

import org.dcm4che.typeddicom.parser.metamodel.dto.*;

import java.util.*;
import java.util.stream.Collectors;
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
                new ArrayList<>(valueRepresentationMetaInfo.dataTypes())
        );
    }

    private DataElementMetaInfoDTO mapDataElementMetaInfoToDataElementMetaInfoDTO(DataElementMetaInfo dataElementMetaInfo) {
        return new DataElementMetaInfoDTO(
                dataElementMetaInfo.getName(),
                null,
                dataElementMetaInfo.getTag(),
                dataElementMetaInfo.getTagConstant(),
                dataElementMetaInfo.getValueRepresentationWrapper(),
                dataElementMetaInfo.getValueMultiplicity(),
                dataElementMetaInfo.getComment(),
                dataElementMetaInfo.isRetired(),
                dataElementMetaInfo.getRetiredSince(),
                dataElementMetaInfo.getContextsOfAdditionalAttributeInfo().entrySet().stream()
                        .map(this::mapAdditionalAttributeInfoContextsEntryToAdditionalAttributeInfoContextsDTO)
                        .collect(Collectors.toList()),
                StreamSupport.stream(dataElementMetaInfo.getSubDataElementMetaInfos().spliterator(), false)
                        .map(DataElementMetaInfo::getKeyword)
                        .collect(Collectors.toList())
        );
    }

    private AdditionalAttributeInfoContextsDTO mapAdditionalAttributeInfoContextsEntryToAdditionalAttributeInfoContextsDTO(Map.Entry<AdditionalAttributeInfo, Set<Context>> additionalAttributeInfoContextsEntry) {
        return new AdditionalAttributeInfoContextsDTO(
                mapAdditionalAttributeInfoToAdditionalAttributeInfoDTO(additionalAttributeInfoContextsEntry.getKey()),
                additionalAttributeInfoContextsEntry.getValue().stream()
                        .map(this::mapContextToContextEntryDTOs)
                        .collect(Collectors.toList())
        );
    }

    private List<ContextEntryDTO> mapContextToContextEntryDTOs(Context context) {
        return context.getContext().stream().map(this::mapContextEntryToContextEntryDTO).collect(Collectors.toList());
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
                        .map(DataElementMetaInfo::getKeyword).collect(Collectors.toList())
        );
    }

    private InformationObjectDefinitionMetaInfoDTO mapInformationObjectDefinitionMetaInfoToInformationObjectDefinitionMetaInfoDTO(InformationObjectDefinitionMetaInfo informationObjectDefinitionMetaInfo) {
        return new InformationObjectDefinitionMetaInfoDTO(
                informationObjectDefinitionMetaInfo.getName(),
                informationObjectDefinitionMetaInfo.getHref(),
                informationObjectDefinitionMetaInfo.getSectionId(),
                informationObjectDefinitionMetaInfo.getDescription(),
                informationObjectDefinitionMetaInfo.getSopClasses().stream()
                        .map(this::mapSopClassToSopClassDTO)
                        .collect(Collectors.toList()),
                informationObjectDefinitionMetaInfo.getModuleReferences().stream()
                        .map(ref -> ref.moduleMetaInfo().getKeyword())
                        .collect(Collectors.toList())
        );
    }

    private SOPClassDTO mapSopClassToSopClassDTO(SOPClass sopClass) {
        return new SOPClassDTO(sopClass.name(), sopClass.uid());
    }
}
