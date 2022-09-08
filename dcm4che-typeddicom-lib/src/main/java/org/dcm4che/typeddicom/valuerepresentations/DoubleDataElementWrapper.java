package org.dcm4che.typeddicom.valuerepresentations;

import org.dcm4che.typeddicom.AttributesWrapper;
import org.dcm4che.typeddicom.DataElementWrapper;

public interface DoubleDataElementWrapper extends DataElementWrapper {
    default double getDouble(double defaultValue) {
        return getAttributes().getDouble(getTag(), defaultValue);
    }

    default void setDouble(double value) {
        getAttributes().setDouble(getTag(), getValueRepresentation(), value);
    }

    interface Setter<D extends DoubleDataElementWrapper, P extends AttributesWrapper> extends DataElementWrapper.Setter<D, P> {
        default P setDouble(double value) {
            getDataElementWrapper().setDouble(value);
            return getParentAttributesWrapper();
        }
    }
}
