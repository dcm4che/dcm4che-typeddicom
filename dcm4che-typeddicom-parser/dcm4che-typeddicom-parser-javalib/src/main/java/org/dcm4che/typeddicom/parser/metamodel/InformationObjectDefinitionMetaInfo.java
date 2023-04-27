package org.dcm4che.typeddicom.parser.metamodel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class InformationObjectDefinitionMetaInfo {
    private String name;
    private String keyword;
    private final String href;
    
    private final String sectionId;
    private final Set<SOPClass> sopClasses = new HashSet<>();
    private final List<IODModuleReference> moduleReferences = new ArrayList<>();
    private String description = null;

    public InformationObjectDefinitionMetaInfo(String name, String keyword, String href, String sectionId) {
        this.name = name;
        this.keyword = keyword;
        this.href = href;
        this.sectionId = sectionId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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

    public Set<SOPClass> getSopClasses() {
        return sopClasses;
    }
    
    public void addSopClass(String name, String uid) {
        this.sopClasses.add(new SOPClass(name, uid));
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
