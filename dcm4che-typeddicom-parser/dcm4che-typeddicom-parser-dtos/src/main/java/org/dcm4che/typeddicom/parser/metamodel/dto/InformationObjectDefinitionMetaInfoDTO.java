package org.dcm4che.typeddicom.parser.metamodel.dto;

import com.fasterxml.jackson.annotation.JsonMerge;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public final class InformationObjectDefinitionMetaInfoDTO {
    private final String name;
    private final String href;
    private final String sectionId;
    private final String description;
    @JsonMerge
    private final List<SOPClassDTO> sopClasses;
    @JsonMerge
    private final List<String> modules;

    public InformationObjectDefinitionMetaInfoDTO(
            @JsonProperty("name") String name,
            @JsonProperty("href") String href,
            @JsonProperty("sectionId") String sectionId,
            @JsonProperty("description") String description,
            @JsonProperty("sopClasses") List<SOPClassDTO> sopClasses,
            @JsonProperty("modules") List<String> modules
    ) {
        this.name = name;
        this.href = href;
        this.sectionId = sectionId;
        this.description = description;
        this.sopClasses = sopClasses;
        this.modules = modules;
    }

    public String getName() {
        return name;
    }

    public String getHref() {
        return href;
    }

    public String getSectionId() {
        return sectionId;
    }

    public String getDescription() {
        return description;
    }

    public List<SOPClassDTO> getSopClasses() {
        return sopClasses;
    }

    public List<String> getModules() {
        return modules;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (InformationObjectDefinitionMetaInfoDTO) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.href, that.href) &&
                Objects.equals(this.sectionId, that.sectionId) &&
                Objects.equals(this.description, that.description) &&
                Objects.equals(this.sopClasses, that.sopClasses) &&
                Objects.equals(this.modules, that.modules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, href, sectionId, description, sopClasses, modules);
    }

    @Override
    public String toString() {
        return "InformationObjectDefinitionMetaInfoDTO[" +
                "name=" + name + ", " +
                "href=" + href + ", " +
                "sectionId=" + sectionId + ", " +
                "description=" + description + ", " +
                "sopClasses=" + sopClasses + ", " +
                "modules=" + modules + ']';
    }
}
