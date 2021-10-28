package com.agfa.typeddicom;

import org.dcm4che3.data.VR;

/**
 * Representation of a Data Element. It contains Attributes, the DICOM Tag and the Value Representation.
 * Accessors are using this data to query the values from the Attributes.
 */
public interface DataElementWrapper extends AttributesWrapper {
    int getTag();

    VR getValueRepresentation();

    Object getValue();

    boolean exists();
}
