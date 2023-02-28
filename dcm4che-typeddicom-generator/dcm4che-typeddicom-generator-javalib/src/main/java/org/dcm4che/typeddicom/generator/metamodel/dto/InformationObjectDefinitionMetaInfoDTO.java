package org.dcm4che.typeddicom.generator.metamodel.dto;

import java.util.List;

public record InformationObjectDefinitionMetaInfoDTO(
        String name,
        String href,
        String sectionId,
        List<SOPClassDTO> sopClasses,
        List<String> modules
) {
}
