package com.agfa.typeddicom.dataelements;

import org.dcm4che3.data.SpecificCharacterSet;
import org.dcm4che3.data.VR;

/**
 * TODO describe this class
 *
 * @author (. @ agfa.com)
 */
public interface DataElementWrapper {
    Object getValue();

    boolean bigEndian();

    VR getValueRepresentation();

    SpecificCharacterSet getSpecificCharacterSet();
}
