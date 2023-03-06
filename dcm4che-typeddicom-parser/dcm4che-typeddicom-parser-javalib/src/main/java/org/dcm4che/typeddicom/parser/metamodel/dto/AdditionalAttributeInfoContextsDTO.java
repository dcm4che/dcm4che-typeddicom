package org.dcm4che.typeddicom.parser.metamodel.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public record AdditionalAttributeInfoContextsDTO(
        AdditionalAttributeInfoDTO additionalAttributeInfoDTO,
        List<List<ContextEntryDTO>> contexts
) {
    @JsonIgnore
    public String getContextsHTML() {
        StringBuilder html = new StringBuilder();
        for (int i = 0; i < contexts().size(); i++) {
            html.append("<li>").append(getSingleContextHTML(i)).append("</li>");
        }
        return html.toString();
    }

    private String getSingleContextHTML(int contextIndex) {
        List<ContextEntryDTO> context = contexts.get(contextIndex);
        StringBuilder html = new StringBuilder();
        for (int i = 0; i < context.size(); i++) {
            html.append("<a href=\"");
            html.append(context.get(i).href());
            html.append("\">");
            html.append(context.get(i).contextName());
            html.append("</a>");
            if (i < context.size() - 1 ) {
                html.append(" &gt; ");
            }
        }
        return html.toString();
    }

}
