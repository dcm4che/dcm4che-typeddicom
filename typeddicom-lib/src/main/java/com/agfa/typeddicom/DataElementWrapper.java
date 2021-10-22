package com.agfa.typeddicom;

import org.dcm4che3.data.VR;

/**
 * TODO describe this class
 *
 * @author (. @ agfa.com)
 */
public interface DataElementWrapper extends AttributesWrapper {
    int getTag();

    VR getValueRepresentation();

    Object getValue();

    boolean exists();
}
