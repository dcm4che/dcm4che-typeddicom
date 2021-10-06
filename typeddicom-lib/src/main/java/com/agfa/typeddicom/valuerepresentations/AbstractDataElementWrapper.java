package com.agfa.typeddicom.valuerepresentations;

import org.dcm4che3.data.SpecificCharacterSet;

import java.util.TimeZone;

/**
 * TODO describe this class
 *
 * @author Niklas Roth (niklas.roth@agfa.com)
 */
abstract public class AbstractDataElementWrapper implements DataElementWrapper {
    private final Object value;
    private final boolean bigEndian;
    private final SpecificCharacterSet specificCharacterSet;
    private final TimeZone timeZone;

    public AbstractDataElementWrapper(Object value, boolean bigEndian, SpecificCharacterSet specificCharacterSet, TimeZone timeZone) {
        this.value = value;
        this.bigEndian = bigEndian;
        this.specificCharacterSet = specificCharacterSet;
        this.timeZone = timeZone;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public boolean bigEndian() {
        return bigEndian;
    }

    @Override
    public SpecificCharacterSet getCharacterSet() {
        return getValueRepresentation().useSpecificCharacterSet() ? specificCharacterSet : SpecificCharacterSet.ASCII;
    }

    @Override
    public TimeZone getTimeZone() {
        return timeZone;
    }
}
