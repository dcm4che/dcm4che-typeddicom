package com.agfa.typeddicom.valuerepresentations;

public interface DoubleDataElementWrapper extends DataElementWrapper {
    default double getDouble(double defaultValue) {
        return getAttributes().getDouble(getTag(), defaultValue);
    }

    default void setDouble(double value) {
        getAttributes().setDouble(getTag(), getValueRepresentation(), value);
    }
}
