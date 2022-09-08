package org.dcm4che.typeddicom.valuerepresentations;

import org.dcm4che.typeddicom.AttributesWrapper;
import org.dcm4che.typeddicom.DataElementWrapper;

public interface IntDataElementWrapper extends DataElementWrapper {
    default int getInt(int defaultValue) {
        return getAttributes().getInt(getTag(), defaultValue);
    }

    default void setInt(int value) {
        getAttributes().setInt(getTag(), getValueRepresentation(), value);
    }

    interface Setter<D extends IntDataElementWrapper, P extends AttributesWrapper> extends DataElementWrapper.Setter<D, P> {
        default P setInt(int value) {
            getDataElementWrapper().setInt(value);
            return getParentAttributesWrapper();
        }
    }
}
