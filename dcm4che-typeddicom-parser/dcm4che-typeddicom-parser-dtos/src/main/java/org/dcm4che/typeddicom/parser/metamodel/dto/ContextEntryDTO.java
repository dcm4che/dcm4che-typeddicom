package org.dcm4che.typeddicom.parser.metamodel.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public final class ContextEntryDTO {
    private final String contextName;
    private final String href;

    public ContextEntryDTO(@JsonProperty("contextName") String contextName, @JsonProperty("href") String href) {
        this.contextName = contextName;
        this.href = href;
    }

    @JsonIgnore
    public String getContextHTML() {
        return "<a href=\"" + this.getHref() + "\">" + this.getContextName() + "</a>";
    }

    public String getContextName() {
        return contextName;
    }

    public String getHref() {
        return href;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ContextEntryDTO) obj;
        return Objects.equals(this.contextName, that.contextName) &&
                Objects.equals(this.href, that.href);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contextName, href);
    }

    @Override
    public String toString() {
        return "ContextEntryDTO[" +
                "contextName=" + contextName + ", " +
                "href=" + href + ']';
    }

}
