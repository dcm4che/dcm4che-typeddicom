package org.dcm4che.typeddicom.parser.metamodel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public final class SOPClassDTO {
    private final String name;
    private final String uid;

    public SOPClassDTO(@JsonProperty("name") String name, @JsonProperty("uid") String uid) {
        this.name = name;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (SOPClassDTO) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.uid, that.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, uid);
    }

    @Override
    public String toString() {
        return "SOPClassDTO[" +
                "name=" + name + ", " +
                "uid=" + uid + ']';
    }

}
