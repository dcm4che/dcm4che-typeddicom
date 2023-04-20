package org.dcm4che.typeddicom.parser.metamodel;

import java.util.Objects;

public final class ContextEntry {
    private final String contextName;
    private final String href;

    public ContextEntry(String contextName, String href) {
        this.contextName = contextName;
        this.href = href;
    }

    public String contextName() {
        return contextName;
    }

    public String href() {
        return href;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ContextEntry) obj;
        return Objects.equals(this.contextName, that.contextName) &&
                Objects.equals(this.href, that.href);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contextName, href);
    }

    @Override
    public String toString() {
        return "ContextEntry[" +
                "contextName=" + contextName + ", " +
                "href=" + href + ']';
    }

}
