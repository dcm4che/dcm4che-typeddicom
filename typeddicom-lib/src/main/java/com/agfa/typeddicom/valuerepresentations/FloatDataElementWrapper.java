package com.agfa.typeddicom.valuerepresentations;

public interface FloatDataElementWrapper extends DataElementWrapper {
    default float getFloat(float defaultValue) {
        return getAttributes().getFloat(getTag(), defaultValue);
    }

    default void setFloat(float value) {
        getAttributes().setFloat(getTag(), getValueRepresentation(), value);
    }
}
