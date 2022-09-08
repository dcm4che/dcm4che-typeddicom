package org.dcm4che.typeddicom.valuerepresentations;

import org.dcm4che.typeddicom.AttributesWrapper;
import org.dcm4che.typeddicom.DataElementWrapper;

public interface DoubleDataElementMultiWrapper extends DataElementWrapper {
    default double[] getDoubles() {
        return getAttributes().getDoubles(getTag());
    }
    
    default double getDouble(int index, double defaultValue) {
        return getAttributes().getDouble(getTag(), index, defaultValue);
    }

    default void setDoubles(double... doubles) {
        getAttributes().setDouble(getTag(), getValueRepresentation(), doubles);
    }

    interface Setter<D extends DoubleDataElementMultiWrapper, P extends AttributesWrapper> extends DataElementWrapper.Setter<D, P> {
        default P setDoubles(double... doubles) {
            getDataElementWrapper().setDoubles(doubles);
            return getParentAttributesWrapper();
        }
    }
}
