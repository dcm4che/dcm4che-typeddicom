package org.dcm4che.typeddicom.valuerepresentations;

import org.dcm4che.typeddicom.Builder;
import org.dcm4che.typeddicom.DataElementWrapper;

public interface IntDataElementWrapper extends DataElementWrapper {
    default int getInt(int defaultValue) {
        return getAttributes().getInt(getTag(), defaultValue);
    }

    default void setInt(int value) {
        getAttributes().setInt(getTag(), getValueRepresentation(), value);
    }

    interface Setter<B extends Builder<B, ?>, D extends IntDataElementWrapper> extends Builder.Setter<B, D> {
        default B asInt(int value) {
            getDataElementWrapper().setInt(value);
            return getBuilder();
        }
    }
}
