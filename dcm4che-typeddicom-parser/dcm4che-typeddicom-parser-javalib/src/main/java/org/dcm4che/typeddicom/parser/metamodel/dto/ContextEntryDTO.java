package org.dcm4che.typeddicom.parser.metamodel.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record ContextEntryDTO(String contextName, String href) {
    @JsonIgnore
    public String getContextHTML() {
        return "<a href=\"" + this.href() + "\">" + this.contextName() + "</a>";
    }
}
