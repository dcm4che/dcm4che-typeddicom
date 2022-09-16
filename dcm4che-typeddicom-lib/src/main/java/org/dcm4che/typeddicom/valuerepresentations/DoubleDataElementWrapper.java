package org.dcm4che.typeddicom.valuerepresentations;

import org.dcm4che.typeddicom.Builder;
import org.dcm4che.typeddicom.DataElementWrapper;

public interface DoubleDataElementWrapper extends DataElementWrapper {
    default double getDouble(double defaultValue) {
        return getAttributes().getDouble(getTag(), defaultValue);
    }

    default void setDouble(double value) {
        getAttributes().setDouble(getTag(), getValueRepresentation(), value);
    }

    interface Setter<B extends Builder<B, ?>, D extends DoubleDataElementWrapper> extends Builder.Setter<B, D> {
        default B asDouble(double value) {
            getDataElementWrapper().setDouble(value);
            return getBuilder();
        }
    }
}
