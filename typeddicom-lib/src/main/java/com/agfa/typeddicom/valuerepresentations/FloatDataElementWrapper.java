package com.agfa.typeddicom.valuerepresentations;

public interface FloatDataElementWrapper extends DataElementWrapper {
    default float getFloat(float defaultValue) {
        return getValueRepresentation().toFloat(getValue(), bigEndian(), 0, defaultValue);
    }
}
