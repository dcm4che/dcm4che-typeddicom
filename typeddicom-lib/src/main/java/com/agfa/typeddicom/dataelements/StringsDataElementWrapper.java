package com.agfa.typeddicom.dataelements;

import org.dcm4che3.data.SpecificCharacterSet;
import org.dcm4che3.data.VR;

public interface StringsDataElementWrapper extends DataElementWrapper {
    default String[] getStrings() {
        VR vr = getValueRepresentation();
        return (String[]) vr.toStrings(
                getValue(),
                bigEndian(),
                getValueRepresentation().useSpecificCharacterSet() ? getSpecificCharacterSet() : SpecificCharacterSet.ASCII
        );
    }
}
