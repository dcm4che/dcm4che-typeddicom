package com.agfa.typeddicom.valuerepresentations;

import com.agfa.typeddicom.AbstractAttributesWrapper;
import org.dcm4che3.data.Attributes;

/**
 * TODO describe this class
 *
 * @author Niklas Roth (niklas.roth@agfa.com)
 */
abstract public class AbstractDataElementWrapper extends AbstractAttributesWrapper implements DataElementWrapper {

    protected AbstractDataElementWrapper(Attributes attributes) {
        super(attributes);
    }

    @Override
    public Object getValue() {
        return getAttributes().getValue(getTag());
    }

    @Override
    public boolean exists() {
        return getAttributes().contains(getTag());
    }
}
