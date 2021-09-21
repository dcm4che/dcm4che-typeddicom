package com.agfa.typeddicom.dataelements;

public interface IntsDataElementWrapper extends DataElementWrapper {
    default int[] getInts(int defaultValue) {
        return getValueRepresentation().toInts(getValue(), bigEndian());
    }
}
