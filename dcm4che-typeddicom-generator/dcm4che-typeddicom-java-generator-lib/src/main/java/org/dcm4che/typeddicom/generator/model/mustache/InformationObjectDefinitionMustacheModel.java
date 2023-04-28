package org.dcm4che.typeddicom.generator.model.mustache;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.dcm4che.typeddicom.generator.JavaDocUtils;
import org.dcm4che.typeddicom.parser.metamodel.dto.InformationObjectDefinitionMetaInfoDTO;
import org.dcm4che.typeddicom.parser.metamodel.dto.SOPClassDTO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class InformationObjectDefinitionMustacheModel {
    private final String keyword;
    private final String name;
    private final String href;
    private final String sectionId;
    private final String description;
    private final List<SOPClassDTO> sopClasses;
    private final List<String> modules;

    public InformationObjectDefinitionMustacheModel(
            String keyword,
            String name,
            String href,
            String sectionId,
            String description,
            List<SOPClassDTO> sopClasses,
            List<String> modules
    ) {
        this.keyword = keyword;
        this.name = name;
        this.href = href;
        this.sectionId = sectionId;
        this.description = description;
        this.sopClasses = sopClasses;
        this.modules = modules;
    }

    public InformationObjectDefinitionMustacheModel(String key, InformationObjectDefinitionMetaInfoDTO dto) {
        this(key, dto.getName(), dto.getHref(), dto.getSectionId(), dto.getDescription(), dto.getSopClasses(),
                dto.getModules());
    }

    public String getImplementsModules() {
        return String.join(", ", modules);
    }

    public String getImplementsModuleBuilders() {
        return modules.stream()
                .map(module -> module + ".Builder<Builder, " + keyword + ">")
                .collect(Collectors.joining(", "));
    }

    public String keyword() {
        return keyword;
    }

    public String name() {
        return name;
    }

    public String href() {
        return href;
    }

    public String sectionId() {
        return sectionId;
    }

    public String description() {
        return description;
    }

    public List<SOPClassDTO> sopClasses() {
        return sopClasses;
    }

    public List<String> modules() {
        return modules;
    }

    @JsonIgnore
    public String getClassJavaDoc() {
        String javadocHtml = "<strong>Name:</strong> " + name + " <br>\n" +
                "<strong>Description:</strong> <br>\n" + description + "\n" +
                "\n" +
                "@see <a href=\"" + href + "\">DICOM Standard Part 3 - " + name + "</a>";

        return JavaDocUtils.javaDocify(javadocHtml, 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (InformationObjectDefinitionMustacheModel) obj;
        return Objects.equals(this.keyword, that.keyword) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.href, that.href) &&
                Objects.equals(this.sectionId, that.sectionId) &&
                Objects.equals(this.description, that.description) &&
                Objects.equals(this.sopClasses, that.sopClasses) &&
                Objects.equals(this.modules, that.modules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyword, name, href, sectionId, description, sopClasses, modules);
    }

    @Override
    public String toString() {
        return "InformationObjectDefinitionMustacheModel[" +
                "keyword=" + keyword + ", " +
                "name=" + name + ", " +
                "href=" + href + ", " +
                "sectionId=" + sectionId + ", " +
                "description=" + description + ", " +
                "sopClasses=" + sopClasses + ", " +
                "modules=" + modules + ']';
    }
}
