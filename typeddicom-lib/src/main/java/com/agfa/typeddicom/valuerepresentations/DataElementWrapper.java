package com.agfa.typeddicom.valuerepresentations;

import org.dcm4che3.data.SpecificCharacterSet;
import org.dcm4che3.data.VR;

import java.util.TimeZone;

/**
 * TODO describe this class
 *
 * @author (. @ agfa.com)
 */
public interface DataElementWrapper {
    Object getValue();

    boolean bigEndian();

    VR getValueRepresentation();
    
    TimeZone getTimeZone();

    SpecificCharacterSet getSpecificCharacterSet();
}
