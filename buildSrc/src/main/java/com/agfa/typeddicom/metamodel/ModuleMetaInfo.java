package com.agfa.typeddicom.metamodel;

import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.agfa.typeddicom.KeywordUtils.sanitizeAsJavaIdentifier;

/**
 * TODO describe this class
 *
 * @author (. @ agfa.com)
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ModuleMetaInfo extends DataElementMetaInfoContainer {
    private final String sectionId;
    private final String name;
    private final String keyword;

    public ModuleMetaInfo(String sectionId, String name) {
        this.sectionId = sectionId;
        this.name = name;
        this.keyword = sanitizeAsJavaIdentifier(name);
    }
}
