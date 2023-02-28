package org.dcm4che.typeddicom.generator.metamodel.dto;

import java.util.List;

public record ValueRepresentationMetaInfoDTO(
        String name,
        String tag,
        String definition,
        String characterRepertoire,
        String lengthOfValue,
        String href,
        List<String> interfaces
) {
}
