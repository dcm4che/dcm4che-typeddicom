package com.agfa.typeddicom.valuerepresentations;

public interface StringDataElementWrapper extends DataElementWrapper {
    default String getString() {
        return getValueRepresentation().toString(getValue(), bigEndian(), 0, null);
    }
}
