package com.agfa.typeddicom.valuerepresentations;

public interface FloatDataElementMultiWrapper extends DataElementWrapper {
    default float[] getFloats() {
        return getValueRepresentation().toFloats(getValue(), bigEndian());
    }
    
    default float getFloat(int index, float defaultValue) {
        return getValueRepresentation().toFloat(getValue(), bigEndian(), index, defaultValue);
    }
}
