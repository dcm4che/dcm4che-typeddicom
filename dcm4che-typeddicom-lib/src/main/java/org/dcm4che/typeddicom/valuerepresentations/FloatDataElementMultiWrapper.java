package org.dcm4che.typeddicom.valuerepresentations;

import org.dcm4che.typeddicom.AttributesWrapper;
import org.dcm4che.typeddicom.DataElementWrapper;

public interface FloatDataElementMultiWrapper extends DataElementWrapper {
    default float[] getFloats() {
        return getAttributes().getFloats(getTag());
    }

    default float getFloat(int index, float defaultValue) {
        return getAttributes().getFloat(getTag(), index, defaultValue);
    }
    
    default void setFloats(float... floats) {
        getAttributes().setFloat(getTag(), getValueRepresentation(), floats);
    }

    interface Setter<D extends FloatDataElementMultiWrapper, P extends AttributesWrapper> extends DataElementWrapper.Setter<D, P> {
        default P setFloats(float... floats) {
            getDataElementWrapper().setFloats(floats);
            return getParentAttributesWrapper();
        }
    }
}
