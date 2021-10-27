package com.agfa.typeddicom;

import org.dcm4che3.data.Attributes;

import java.lang.reflect.InvocationTargetException;

public interface AttributesWrapper {
    static <T extends AttributesWrapper> T wrap(Attributes attributes, Class<T> wrapperClass) {
        try {
            return wrapperClass.getConstructor(Attributes.class).newInstance(attributes);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ConstructorNotImplementedException(wrapperClass.getName() + " could not be instantiated with Attributes.", e);
        }
    }

    Attributes getAttributes();

    default <T extends AttributesWrapper> T copy(Class<T> wrapperClass) {
        return wrap(this.getAttributes(), wrapperClass);
    }

    default boolean isEmpty() {
        return getAttributes().isEmpty();
    }
}
