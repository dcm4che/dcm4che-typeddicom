package com.agfa.typeddicom.metamodel;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.agfa.typeddicom.KeywordUtils.sanitizeAsJavaIdentifier;

/**
 * TODO describe this class
 *
 * @author (. @ agfa.com)
 */
@Data
public class ModuleMetaInfo implements Serializable {
    private final String sectionId;
    private final String name;
    private final String keyword;
    private final List<AttributeMetaInfo> attributeMetaInfos = new ArrayList<>();

    public ModuleMetaInfo(String sectionId, String name) {
        this.sectionId = sectionId;
        this.name = name;
        this.keyword = sanitizeAsJavaIdentifier(name);
    }
}
