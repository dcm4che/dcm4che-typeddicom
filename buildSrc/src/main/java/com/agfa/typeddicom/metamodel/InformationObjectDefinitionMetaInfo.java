package com.agfa.typeddicom.metamodel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO describe this class
 *
 * @author (. @ agfa.com)
 */
public class InformationObjectDefinitionMetaInfo {
    private final String name;
    private final String keyword;
    private final String href;
    private final List<IODModuleReference> moduleReferences = new ArrayList<>();

    public InformationObjectDefinitionMetaInfo(String name, String keyword, String href) {
        this.name = name;
        this.keyword = keyword;
        this.href = href;
    }
    
    public String getImplementsModules() {
        return moduleReferences.stream().map(ref -> ref.moduleMetaInfo().getKeyword()).collect(Collectors.joining(", "));
    }

    public String getName() {
        return name;
    }

    public String getKeyword() {
        return keyword;
    }

    public List<IODModuleReference> getModuleReferences() {
        return moduleReferences;
    }

    public String getHref() {
        return href;
    }
}
