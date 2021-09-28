package com.agfa.typeddicom.valuerepresentations;

public interface DoubleDataElementWrapper extends DataElementWrapper {
    default double getDouble(double defaultValue) {
        return getValueRepresentation().toDouble(getValue(), bigEndian(), 0, defaultValue);
    }
}
