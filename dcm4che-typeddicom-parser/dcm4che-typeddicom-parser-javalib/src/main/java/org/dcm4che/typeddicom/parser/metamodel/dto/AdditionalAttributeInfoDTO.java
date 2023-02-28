package org.dcm4che.typeddicom.parser.metamodel.dto;

public record AdditionalAttributeInfoDTO(
        String name,
        String type,
        String attributeDescription
) {
}
