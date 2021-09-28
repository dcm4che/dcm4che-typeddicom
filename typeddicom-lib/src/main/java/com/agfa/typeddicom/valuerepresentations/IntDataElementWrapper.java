package com.agfa.typeddicom.valuerepresentations;

public interface IntDataElementWrapper extends DataElementWrapper {
    default int getInt(int defaultValue) {
        return getValueRepresentation().toInt(getValue(), bigEndian(), 0, defaultValue);
    }
}
