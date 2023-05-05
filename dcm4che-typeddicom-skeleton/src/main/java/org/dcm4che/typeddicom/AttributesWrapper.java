package org.dcm4che.typeddicom;

import org.dcm4che3.data.Attributes;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

    static IntStream getKnownAttributeTags(Class<? extends AttributesWrapper> attributesWrapperClass) {
        return getImplementedDataElementHolders(attributesWrapperClass)
                .map(Class::getDeclaringClass)
                .mapToInt(clazz -> {
                    try {
                        return clazz.getField("TAG").getInt(null);
                    } catch (IllegalAccessException | NoSuchFieldException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @SuppressWarnings({
            "unchecked", // It's okay we are checking the type by reflection
            "java:S1872", // We want a simple name comparison and no instanceof because there are multiple nested classes with that name
    })
    static Stream<Class<? extends AttributesWrapper>> getImplementedDataElementHolders(Class<? extends AttributesWrapper> clazz) {
        if ("Holder".equals(clazz.getSimpleName())) {
            return Stream.of(clazz);
        } else {
            return Arrays.stream(clazz.getInterfaces())
                    .filter(AttributesWrapper.class::isAssignableFrom)
                    .map(parent -> (Class<? extends AttributesWrapper>) parent)
                    .flatMap(AttributesWrapper::getImplementedDataElementHolders);
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
     * @param <T> the Type of the class to wrap new Attributes object in.
     * @param wrapperClass the class to wrap the new Attributes object in.
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

    default int[] getUnknownAttributeTags() {
        Set<Integer> knownAttributeTags = getKnownAttributeTags(this.getClass())
                .boxed()
                .collect(Collectors.toUnmodifiableSet());
        return Arrays.stream(this.getAttributes().tags())
                .filter(tag -> !knownAttributeTags.contains(tag)).toArray();
    }

    default int[] getKnownAttributeTags() {
        return getKnownAttributeTags(this.getClass()).toArray();
    }
}
