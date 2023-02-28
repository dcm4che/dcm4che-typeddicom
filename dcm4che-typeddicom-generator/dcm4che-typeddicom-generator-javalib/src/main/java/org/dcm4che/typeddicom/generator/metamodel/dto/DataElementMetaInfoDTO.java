package org.dcm4che.typeddicom.generator.metamodel.dto;


import java.util.List;

public record DataElementMetaInfoDTO(
        String name,
        String tag,
        String valueRepresentation,
        String valueMultiplicity,
        String comment,
        boolean retired,
        String retiredSince,
        List<AdditionalAttributeInfoContextsDTO> additionalAttributeInfo
) {
}
