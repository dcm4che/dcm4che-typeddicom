package org.dcm4che.typeddicom.generator.metamodel.dto;

public record AdditionalAttributeInfoDTO(
        String name,
        String type,
        String attributeDescription
) {
}
