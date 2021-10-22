package com.agfa.typeddicom.valuerepresentations;

import com.agfa.typeddicom.DataElementWrapper;

public interface FloatDataElementMultiWrapper extends DataElementWrapper {
    default float[] getFloats() {
        return getAttributes().getFloats(getTag());
    }

    default float getFloat(int index, float defaultValue) {
        return getAttributes().getFloat(getTag(), index, defaultValue);
    }
    
    default void setFloats(float[] floats) {
        getAttributes().setFloat(getTag(), getValueRepresentation(), floats);
    }
}
