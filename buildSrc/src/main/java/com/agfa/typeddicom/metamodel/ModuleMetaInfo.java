package com.agfa.typeddicom.metamodel;

import com.agfa.typeddicom.table.ModuleTable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.agfa.typeddicom.utils.KeywordUtils.sanitizeAsJavaIdentifier;

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
    private final String href;

    public ModuleMetaInfo(ModuleTable moduleTable) {
        this.sectionId = moduleTable.getSectionId();
        this.name = moduleTable.getName();
        this.keyword = moduleTable.getKeyword();
        this.href = moduleTable.getHref();
    }
}
