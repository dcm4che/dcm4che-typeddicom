package com.agfa.typeddicom.valuerepresentations;

import com.agfa.typeddicom.DataElementWrapper;

public interface DoubleDataElementWrapper extends DataElementWrapper {
    default double getDouble(double defaultValue) {
        return getAttributes().getDouble(getTag(), defaultValue);
    }

    default void setDouble(double value) {
        getAttributes().setDouble(getTag(), getValueRepresentation(), value);
    }
}
