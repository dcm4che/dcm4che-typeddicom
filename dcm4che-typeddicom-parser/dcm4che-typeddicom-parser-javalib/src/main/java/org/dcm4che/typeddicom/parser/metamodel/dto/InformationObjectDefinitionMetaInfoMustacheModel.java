package org.dcm4che.typeddicom.parser.metamodel.dto;

import java.util.List;
import java.util.stream.Collectors;

public record InformationObjectDefinitionMetaInfoMustacheModel(
        String keyword,
        String name,
        String href,
        String sectionId,
        List<SOPClassDTO> sopClasses,
        List<String> modules
) {
    public InformationObjectDefinitionMetaInfoMustacheModel(String key, InformationObjectDefinitionMetaInfoDTO dto) {
        this(key, dto.name(), dto.name(), dto.href(), dto.sopClasses(), dto.modules());
    }

    public String getImplementsModules() {
        return String.join(", ", modules);
    }

    public String getImplementsModuleBuilders() {
        return modules.stream()
                .map(module -> module + ".Builder<Builder, " + keyword + ">")
                .collect(Collectors.joining(", "));
    }

}
