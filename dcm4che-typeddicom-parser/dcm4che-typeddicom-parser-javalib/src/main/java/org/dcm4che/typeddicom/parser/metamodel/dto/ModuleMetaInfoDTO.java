package org.dcm4che.typeddicom.parser.metamodel.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class ModuleMetaInfoDTO {
    private final String name;
    private final String sectionId;
    private final String href;
    private final List<String> contains;

    public ModuleMetaInfoDTO(
            @JsonProperty("name") String name,
            @JsonProperty("sectionId") String sectionId,
            @JsonProperty("href") String href,
            @JsonProperty("contains") List<String> contains
    ) {
        this.name = name;
        this.sectionId = sectionId;
        this.href = href;
        this.contains = contains;
    }

    @JsonIgnore
    public String implementsBuilderInterfaces() {
        return contains.stream().map(key -> key + ".Builder<SELF, T>").collect(Collectors.joining(", "));
    }

    @JsonIgnore
    public String implementsHolderInterfaces() {
        return contains.stream().map(key -> key + ".Holder").collect(Collectors.joining(", "));
    }

    public String getName() {
        return name;
    }

    public String getSectionId() {
        return sectionId;
    }

    public String getHref() {
        return href;
    }

    public List<String> getContains() {
        return contains;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ModuleMetaInfoDTO) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.sectionId, that.sectionId) &&
                Objects.equals(this.href, that.href) &&
                Objects.equals(this.contains, that.contains);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, sectionId, href, contains);
    }

    @Override
    public String toString() {
        return "ModuleMetaInfoDTO[" +
                "name=" + name + ", " +
                "sectionId=" + sectionId + ", " +
                "href=" + href + ", " +
                "contains=" + contains + ']';
    }

}
