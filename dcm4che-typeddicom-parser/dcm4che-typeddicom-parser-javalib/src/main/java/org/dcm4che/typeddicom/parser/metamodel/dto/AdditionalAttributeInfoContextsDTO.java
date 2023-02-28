package org.dcm4che.typeddicom.parser.metamodel.dto;

import java.util.List;

public record AdditionalAttributeInfoContextsDTO(
        AdditionalAttributeInfoDTO additionalAttributeInfoDTO,
        List<List<ContextEntryDTO>> contexts
) {
}
