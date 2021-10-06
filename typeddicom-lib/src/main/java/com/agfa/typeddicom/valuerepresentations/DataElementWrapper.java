package com.agfa.typeddicom.valuerepresentations;

import com.agfa.typeddicom.AttributesWrapper;
import org.dcm4che3.data.SpecificCharacterSet;
import org.dcm4che3.data.VR;

import java.util.TimeZone;

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
