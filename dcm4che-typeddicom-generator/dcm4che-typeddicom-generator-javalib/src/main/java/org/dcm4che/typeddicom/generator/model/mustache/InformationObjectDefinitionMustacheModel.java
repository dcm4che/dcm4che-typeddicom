package org.dcm4che.typeddicom.generator.model.mustache;

import org.dcm4che.typeddicom.parser.metamodel.dto.InformationObjectDefinitionMetaInfoDTO;
import org.dcm4che.typeddicom.parser.metamodel.dto.SOPClassDTO;

import java.util.List;
import java.util.stream.Collectors;

public record InformationObjectDefinitionMustacheModel(
        String keyword,
        String name,
        String href,
        String sectionId,
        List<SOPClassDTO> sopClasses,
        List<String> modules
) {
    public InformationObjectDefinitionMustacheModel(String key, InformationObjectDefinitionMetaInfoDTO dto) {
        this(key, dto.name(), dto.href(), dto.sectionId(), dto.sopClasses(), dto.modules());
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
