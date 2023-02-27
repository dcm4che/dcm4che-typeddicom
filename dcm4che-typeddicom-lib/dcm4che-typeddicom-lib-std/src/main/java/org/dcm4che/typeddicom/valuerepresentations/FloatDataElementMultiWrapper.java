package org.dcm4che.typeddicom.valuerepresentations;

import org.dcm4che.typeddicom.Builder;
import org.dcm4che.typeddicom.DataElementWrapper;

public interface FloatDataElementMultiWrapper extends DataElementWrapper {
    default float[] getFloats() {
        return getAttributes().getFloats(getPrivateCreator(), getTag());
    }

    default float getFloat(int index, float defaultValue) {
        return getAttributes().getFloat(getPrivateCreator(), getTag(), index, defaultValue);
    }
    
    default void setFloats(float... floats) {
        getAttributes().setFloat(getPrivateCreator(), getTag(), getValueRepresentation(), floats);
    }

    interface Setter<B extends Builder<B, ?>, D extends FloatDataElementMultiWrapper> extends org.dcm4che.typeddicom.Setter<B, D> {
        default B asFloats(float... floats) {
            getDataElementWrapper().setFloats(floats);
            return getBuilder();
        }
    }
}
