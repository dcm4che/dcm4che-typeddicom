package com.agfa.typeddicom.valuerepresentations;

public interface IntDataElementMultiWrapper extends DataElementWrapper {
    default int[] getInts() {
        return getValueRepresentation().toInts(getValue(), bigEndian());
    }
    
    default int getInts(int index, int defaultValue) {
        return getValueRepresentation().toInt(getValue(), bigEndian(), index, defaultValue);
    }
}
