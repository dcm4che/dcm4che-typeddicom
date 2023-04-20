package org.dcm4che.typeddicom.parser.metamodel;

import java.util.Objects;
import java.util.Set;

public final class ValueRepresentationMetaInfo {
    private final String tag;
    private final String name;
    private final String keyword;
    private final String definition;
    private final String characterRepertoire;
    private final String lengthOfValue;
    private final String href;
    private final Set<String> dataTypes;

    public ValueRepresentationMetaInfo(String tag, String name, String keyword, String definition,
                                       String characterRepertoire, String lengthOfValue,
                                       String href, Set<String> dataTypes) {
        this.tag = tag;
        this.name = name;
        this.keyword = keyword;
        this.definition = definition;
        this.characterRepertoire = characterRepertoire;
        this.lengthOfValue = lengthOfValue;
        this.href = href;
        this.dataTypes = dataTypes;
    }

    public String tag() {
        return tag;
    }

    public String name() {
        return name;
    }

    public String keyword() {
        return keyword;
    }

    public String definition() {
        return definition;
    }

    public String characterRepertoire() {
        return characterRepertoire;
    }

    public String lengthOfValue() {
        return lengthOfValue;
    }

    public String href() {
        return href;
    }

    public Set<String> dataTypes() {
        return dataTypes;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ValueRepresentationMetaInfo) obj;
        return Objects.equals(this.tag, that.tag) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.keyword, that.keyword) &&
                Objects.equals(this.definition, that.definition) &&
                Objects.equals(this.characterRepertoire, that.characterRepertoire) &&
                Objects.equals(this.lengthOfValue, that.lengthOfValue) &&
                Objects.equals(this.href, that.href) &&
                Objects.equals(this.dataTypes, that.dataTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag, name, keyword, definition, characterRepertoire, lengthOfValue, href, dataTypes);
    }

    @Override
    public String toString() {
        return "ValueRepresentationMetaInfo[" +
                "tag=" + tag + ", " +
                "name=" + name + ", " +
                "keyword=" + keyword + ", " +
                "definition=" + definition + ", " +
                "characterRepertoire=" + characterRepertoire + ", " +
                "lengthOfValue=" + lengthOfValue + ", " +
                "href=" + href + ", " +
                "dataTypes=" + dataTypes + ']';
    }

}
