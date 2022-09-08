package org.dcm4che.typeddicom.valuerepresentations;

import org.dcm4che.typeddicom.AttributesWrapper;
import org.dcm4che.typeddicom.DataElementWrapper;

public interface FloatDataElementWrapper extends DataElementWrapper {
    default float getFloat(float defaultValue) {
        return getAttributes().getFloat(getTag(), defaultValue);
    }

    default void setFloat(float value) {
        getAttributes().setFloat(getTag(), getValueRepresentation(), value);
    }

    interface Setter<D extends FloatDataElementWrapper, P extends AttributesWrapper> extends DataElementWrapper.Setter<D, P> {
        default P setFloat(float value) {
            getDataElementWrapper().setFloat(value);
            return getParentAttributesWrapper();
        }
    }
}
