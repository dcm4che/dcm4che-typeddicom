package org.dcm4che.typeddicom.valuerepresentations;

import org.dcm4che.typeddicom.DataElementWrapper;

public interface FloatDataElementWrapper extends DataElementWrapper {
    default float getFloat(float defaultValue) {
        return getAttributes().getFloat(getTag(), defaultValue);
    }

    default void setFloat(float value) {
        getAttributes().setFloat(getTag(), getValueRepresentation(), value);
    }
}
