package org.dcm4che.typeddicom.valuerepresentations;

import org.dcm4che.typeddicom.DataElementWrapper;

public interface StringDataElementWrapper extends DataElementWrapper {
    default String getString() {
        return getAttributes().getString(getTag());
    }

    default void setString(String string) {
        getAttributes().setString(getTag(), getValueRepresentation(), string);
    }
}
