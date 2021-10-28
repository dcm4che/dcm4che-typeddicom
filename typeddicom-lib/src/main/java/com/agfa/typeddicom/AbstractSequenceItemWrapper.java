package com.agfa.typeddicom;

import org.dcm4che3.data.Attributes;

/**
 * This class represents a wrapper for Attributes which are Sequence items. It has a different {@link #toString} method
 * which adds the class name with the enclosing class in front of the contained attributes.
 */
public abstract class AbstractSequenceItemWrapper extends AbstractAttributesWrapper {
    protected AbstractSequenceItemWrapper(Attributes attributes) {
        super(attributes);
    }

    protected AbstractSequenceItemWrapper() {
        super();
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        Class<?> clazz = this.getClass().getEnclosingClass();
        while (clazz != null) {
            string.insert(0, clazz.getSimpleName() + '.');
            clazz = clazz.getEnclosingClass();
        }
        string.append(super.toString());
        return string.toString();
    }
}
