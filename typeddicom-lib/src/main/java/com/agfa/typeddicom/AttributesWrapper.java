package com.agfa.typeddicom;

import org.dcm4che3.data.Attributes;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;


/**
 * TODO describe this class
 *
 * @author Niklas Roth (niklas.roth@agfa.com)
 */
public abstract class AttributesWrapper {
    private final Attributes attributes;

    public AttributesWrapper(Attributes attributes) {
        this.attributes = attributes;
    }

    public static <T extends AttributesWrapper> T wrap(Attributes attributes, Class<T> wrapperClass) {
        try {
            return wrapperClass.getConstructor(Attributes.class).newInstance(attributes);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ConstructorNotImplementedException(wrapperClass.getName() + " could not be instantiated with Attributes.", e);
        }
    }

    public Attributes getAttributes() {
        return attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttributesWrapper that = (AttributesWrapper) o;
        return attributes.equals(that.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attributes);
    }
}
