package com.agfa.typeddicom.valuerepresentations;

import com.agfa.typeddicom.DataElementWrapper;

public interface StringDataElementWrapper extends DataElementWrapper {
    default String getString() {
        return getAttributes().getString(getTag());
    }

    default void setString(String[] string) {
        getAttributes().setString(getTag(), getValueRepresentation(), string);
    }
}
