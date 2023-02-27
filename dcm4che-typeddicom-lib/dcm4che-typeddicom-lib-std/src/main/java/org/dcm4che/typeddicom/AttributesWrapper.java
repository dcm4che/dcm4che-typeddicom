package org.dcm4che.typeddicom;

import org.dcm4che3.data.Attributes;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public interface AttributesWrapper {
    static <T extends AttributesWrapper> T wrap(Attributes attributes, Class<T> wrapperClass) {
        try {
            final Constructor<T> constructor = wrapperClass.getDeclaredConstructor(Attributes.class);
            constructor.setAccessible(true);
            return constructor.newInstance(attributes);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ConstructorNotImplementedException(wrapperClass.getName() + " could not be instantiated with Attributes.", e);
        }
    }

    /**
     * Get the wrapped Attributes.
     *
     * @return the wrapped Attributes object.
     */
    Attributes getAttributes();

    /**
     * Returns a new Attributes wrapper instance which wraps a copy of the attributes contained in this 
     * Attributes wrapper. (see {@link Attributes#Attributes(Attributes)}
     * 
     * @param wrapperClass The class to wrap the new Attributes object in.
     * @return a new instance of an Attributes wrapper implementation which wraps a copy of the attributes contained in 
     * this Attributes wrapper.
     */
    default <T extends AttributesWrapper> T copy(Class<T> wrapperClass) {
        return wrap(new Attributes(this.getAttributes()), wrapperClass);
    }

    /**
     * See also {@link Attributes#isEmpty()}.
     * 
     * @return <code>true</code> if the wrapped attributes are empty, <code>false</code> otherwise.
     */
    default boolean isEmpty() {
        return getAttributes().isEmpty();
    }
}
