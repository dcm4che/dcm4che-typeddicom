package com.agfa.typeddicom.valuerepresentations;

public interface IntDataElementMultiWrapper extends DataElementWrapper {
    default int[] getInts() {
        return getAttributes().getInts(getTag());
    }
    
    default int getInt(int index, int defaultValue) {
        return getAttributes().getInt(getTag(), index, defaultValue);
    }
    
    default void setInts(int[] ints) {
        getAttributes().setInt(getTag(), getValueRepresentation(), ints);
    }
}
