package com.agfa.typeddicom.metamodel;

import com.agfa.typeddicom.table.ModuleTable;

import java.util.Objects;

import static com.agfa.typeddicom.utils.KeywordUtils.sanitizeAsJavaIdentifier;

/**
 * TODO describe this class
 *
 * @author (. @ agfa.com)
 */
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

    public ModuleMetaInfo(String sectionId, String name, String keyword, String href) {
        this.sectionId = sectionId;
        this.name = name;
        this.keyword = keyword;
        this.href = href;
    }

    public String getSectionId() {
        return sectionId;
    }

    public String getName() {
        return name;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getHref() {
        return href;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ModuleMetaInfo that = (ModuleMetaInfo) o;
        return Objects.equals(sectionId, that.sectionId) && Objects.equals(name, that.name) && Objects.equals(keyword, that.keyword) && Objects.equals(href, that.href);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sectionId, name, keyword, href);
    }
}
