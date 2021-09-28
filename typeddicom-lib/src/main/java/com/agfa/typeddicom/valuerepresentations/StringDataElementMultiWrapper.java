package com.agfa.typeddicom.valuerepresentations;

import org.dcm4che3.data.SpecificCharacterSet;
import org.dcm4che3.data.VR;

public interface StringDataElementMultiWrapper extends DataElementWrapper {
    default String[] getStrings() {
        VR vr = getValueRepresentation();
        return (String[]) vr.toStrings(
                getValue(),
                bigEndian(),
                getValueRepresentation().useSpecificCharacterSet() ? getSpecificCharacterSet() : SpecificCharacterSet.ASCII
        );
    }
    
    default String getString(int index, String defaultValue) {
        VR vr = getValueRepresentation();
        return vr.toString(
                getValue(),
                bigEndian(),
                index,
                defaultValue
        );
    }
}
