package com.agfa.typeddicom.valuerepresentations;

public interface StringDataElementWrapper extends DataElementWrapper {
    default String getString() {
        return (String) getValueRepresentation().toStrings(getValue(), bigEndian(), getCharacterSet());
    }
}
