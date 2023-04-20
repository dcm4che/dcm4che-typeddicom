package org.dcm4che.typeddicom.parser.metamodel;

import java.util.Objects;

public final class SOPClass {
    private final String name;
    private final String uid;

    public SOPClass(String name, String uid) {
        this.name = name;
        this.uid = uid;
    }

    public String name() {
        return name;
    }

    public String uid() {
        return uid;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (SOPClass) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.uid, that.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, uid);
    }

    @Override
    public String toString() {
        return "SOPClass[" +
                "name=" + name + ", " +
                "uid=" + uid + ']';
    }

}
