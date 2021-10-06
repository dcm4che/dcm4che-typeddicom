package com.agfa.typeddicom.valuerepresentations;

public interface IntDataElementWrapper extends DataElementWrapper {
    default int getInt(int defaultValue) {
        return getAttributes().getInt(getTag(), defaultValue);
    }

    default void setInt(int value) {
        getAttributes().setInt(getTag(), getValueRepresentation(), value);
    }
}
