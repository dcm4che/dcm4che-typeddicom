package org.dcm4che.typeddicom.parser.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.Arrays;

public class RecordUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecordUtils.class);

    private RecordUtils() {}

    public static <T> T combineRecords(T base, T override, Class<T> recordClass) {
        try {
            Constructor<T> constructor = recordClass.getConstructor(Arrays.stream(recordClass.getRecordComponents()).map(RecordComponent::getType).toArray(Class[]::new));
            ArrayList<Object> constructorArgs = new ArrayList<>();

            for (RecordComponent recordComponent : recordClass.getRecordComponents()) {
                Object oldValue = recordComponent.getAccessor().invoke(base);
                Object newValue = recordComponent.getAccessor().invoke(override);
                constructorArgs.add(newValue != null ? newValue : oldValue);
            }
            return constructor.newInstance(constructorArgs.toArray());
        } catch (ReflectiveOperationException e) {
            LOGGER.error("Something went wrong while combining record objects.\nbase: {}\noverride: {}", base, override, e);
            throw new IllegalStateException("Something went wrong while combining record objects.", e);
        }
    }
}
