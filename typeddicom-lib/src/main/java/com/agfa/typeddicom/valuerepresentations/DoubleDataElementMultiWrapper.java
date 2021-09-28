package com.agfa.typeddicom.valuerepresentations;

public interface DoubleDataElementMultiWrapper extends DataElementWrapper {
    default double[] getDoubles() {
        return getValueRepresentation().toDoubles(getValue(), bigEndian());
    }
    
    default double getDouble(int index, double defaultValue) {
        return getValueRepresentation().toDouble(getValue(), bigEndian(), index, defaultValue);
    }
}
