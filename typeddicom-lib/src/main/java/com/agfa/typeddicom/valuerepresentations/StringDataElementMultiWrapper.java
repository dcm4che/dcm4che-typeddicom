package com.agfa.typeddicom.valuerepresentations;

import org.dcm4che3.data.SpecificCharacterSet;
import org.dcm4che3.data.VR;

public interface StringDataElementMultiWrapper extends DataElementWrapper {
    default String[] getStrings() {
        return getAttributes().getStrings(getTag());
    }

    default String getString(int index) {
        return getAttributes().getString(getTag(), index);
    }

    default String getString(int index, String defaultValue) {
        return getAttributes().getString(getTag(), index, defaultValue);
    }

    default void setStrings(String[] strings) {
        getAttributes().setString(getTag(), getValueRepresentation(), strings);
    }
}
