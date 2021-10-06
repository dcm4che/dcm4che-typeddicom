package com.agfa.typeddicom;

import org.dcm4che3.data.Attributes;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;


/**
 * TODO describe this class
 *
 * @author Niklas Roth (niklas.roth@agfa.com)
 */
public abstract class AbstractAttributesWrapper implements AttributesWrapper {
    private final Attributes attributes;

    public AbstractAttributesWrapper(Attributes attributes) {
        this.attributes = attributes;
    }

    public static <T extends AbstractAttributesWrapper> T wrap(Attributes attributes, Class<T> wrapperClass) {
        try {
            return wrapperClass.getConstructor(Attributes.class).newInstance(attributes);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ConstructorNotImplementedException(wrapperClass.getName() + " could not be instantiated with Attributes.", e);
        }
    }

    @Override
    public Attributes getAttributes() {
        return attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractAttributesWrapper that = (AbstractAttributesWrapper) o;
        return attributes.equals(that.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attributes);
    }
}
