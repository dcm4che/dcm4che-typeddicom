package com.agfa.typeddicom.metamodel;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO describe this class
 *
 * @author (. @ agfa.com)
 */
@Data
public class InformationObjectDefinitionMetaInfo {
    private final String name;
    private final String keyword;
    private final List<IODModuleReference> moduleReferences = new ArrayList<>();

    public InformationObjectDefinitionMetaInfo(String name, String keyword) {
        this.name = name;
        this.keyword = keyword;
    }
    
    public String getImplementsModules() {
        return moduleReferences.stream().map(ref -> ref.moduleMetaInfo().getKeyword()).collect(Collectors.joining(", "));
    }
}
