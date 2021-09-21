package com.agfa.typeddicom.dataelements;

import org.dcm4che3.data.SpecificCharacterSet;

/**
 * TODO describe this class
 *
 * @author Niklas Roth (niklas.roth@agfa.com)
 */
abstract public class AbstractDataElementWrapper implements DataElementWrapper {
    private final Object value;
    private final boolean bigEndian;
    private final SpecificCharacterSet characterSet;

    public AbstractDataElementWrapper(Object value, boolean bigEndian, SpecificCharacterSet characterSet) {
        this.value = value;
        this.bigEndian = bigEndian;
        this.characterSet = characterSet;
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
    public SpecificCharacterSet getSpecificCharacterSet() {
        return characterSet;
    }
}
