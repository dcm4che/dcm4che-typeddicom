package com.agfa.typeddicom.valuerepresentations;

import com.agfa.typeddicom.DataElementWrapper;

public interface DoubleDataElementMultiWrapper extends DataElementWrapper {
    default double[] getDoubles() {
        return getAttributes().getDoubles(getTag());
    }
    
    default double getDouble(int index, double defaultValue) {
        return getAttributes().getDouble(getTag(), index, defaultValue);
    }

    default void setDoubles(double... doubles) {
        getAttributes().setDouble(getTag(), getValueRepresentation(), doubles);
    }
}
