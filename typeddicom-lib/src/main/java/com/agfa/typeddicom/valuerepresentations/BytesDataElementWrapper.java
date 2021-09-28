package com.agfa.typeddicom.valuerepresentations;

public interface BytesDataElementWrapper extends DataElementWrapper {
    default byte[] getBytes() {
        return getValueRepresentation().toBytes(getValue(), getSpecificCharacterSet());
    }
}
