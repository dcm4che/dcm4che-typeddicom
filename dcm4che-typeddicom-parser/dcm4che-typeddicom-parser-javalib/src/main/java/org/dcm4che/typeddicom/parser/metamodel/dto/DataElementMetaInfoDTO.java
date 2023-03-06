package org.dcm4che.typeddicom.parser.metamodel.dto;

import java.util.List;

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
}
