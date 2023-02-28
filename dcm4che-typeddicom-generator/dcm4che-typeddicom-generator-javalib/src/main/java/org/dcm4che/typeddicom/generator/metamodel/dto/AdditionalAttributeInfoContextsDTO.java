package org.dcm4che.typeddicom.generator.metamodel.dto;

import java.util.List;

public record AdditionalAttributeInfoContextsDTO(
        AdditionalAttributeInfoDTO additionalAttributeInfoDTO,
        List<List<ContextEntryDTO>> contexts
) {
}
