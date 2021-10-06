package com.agfa.typeddicom.valuerepresentations;

public interface StringDataElementWrapper extends DataElementWrapper {
    default String getString() {
        return getAttributes().getString(getTag());
    }

    default void setString(String[] string) {
        getAttributes().setString(getTag(), getValueRepresentation(), string);
    }
}
