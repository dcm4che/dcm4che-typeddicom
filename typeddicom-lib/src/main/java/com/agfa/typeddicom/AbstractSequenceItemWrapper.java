package com.agfa.typeddicom;

import org.dcm4che3.data.Attributes;

public abstract class AbstractSequenceItemWrapper extends AbstractAttributesWrapper {
    public AbstractSequenceItemWrapper(Attributes attributes) {
        super(attributes);
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
