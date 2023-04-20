package org.dcm4che.typeddicom.parser.metamodel.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonMerge;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public final class AdditionalAttributeInfoContextsDTO {
    private final AdditionalAttributeInfoDTO additionalAttributeInfoDTO;
    @JsonMerge
    private final List<List<ContextEntryDTO>> contexts;

    public AdditionalAttributeInfoContextsDTO(
            @JsonProperty("additionalAttributeInfoDTO") AdditionalAttributeInfoDTO additionalAttributeInfoDTO,
            @JsonProperty("contexts") List<List<ContextEntryDTO>> contexts
    ) {
        this.additionalAttributeInfoDTO = additionalAttributeInfoDTO;
        this.contexts = contexts;
    }

    @JsonIgnore
    public String getContextsHTML() {
        StringBuilder html = new StringBuilder();
        for (int i = 0; i < getContexts().size(); i++) {
            html.append("<li>").append(getSingleContextHTML(i)).append("</li>");
        }
        return html.toString();
    }

    private String getSingleContextHTML(int contextIndex) {
        List<ContextEntryDTO> context = contexts.get(contextIndex);
        StringBuilder html = new StringBuilder();
        for (int i = 0; i < context.size(); i++) {
            html.append("<a href=\"");
            html.append(context.get(i).getHref());
            html.append("\">");
            html.append(context.get(i).getContextName());
            html.append("</a>");
            if (i < context.size() - 1) {
                html.append(" &gt; ");
            }
        }
        return html.toString();
    }

    public AdditionalAttributeInfoDTO getAdditionalAttributeInfoDTO() {
        return additionalAttributeInfoDTO;
    }

    public List<List<ContextEntryDTO>> getContexts() {
        return contexts;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (AdditionalAttributeInfoContextsDTO) obj;
        return Objects.equals(this.additionalAttributeInfoDTO, that.additionalAttributeInfoDTO) &&
                Objects.equals(this.contexts, that.contexts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(additionalAttributeInfoDTO, contexts);
    }

    @Override
    public String toString() {
        return "AdditionalAttributeInfoContextsDTO[" +
                "additionalAttributeInfoDTO=" + additionalAttributeInfoDTO + ", " +
                "contexts=" + contexts + ']';
    }


}
