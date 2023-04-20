package org.dcm4che.typeddicom.valuerepresentations;

import org.dcm4che.typeddicom.Builder;
import org.dcm4che.typeddicom.DataElementWrapper;

public interface DoubleDataElementMultiWrapper extends DataElementWrapper {
    default double[] getDoubles() {
        return getAttributes().getDoubles(getPrivateCreator(), getTag());
    }
    
    default double getDouble(int index, double defaultValue) {
        return getAttributes().getDouble(getPrivateCreator(), getTag(), index, defaultValue);
    }

    default void setDoubles(double... doubles) {
        getAttributes().setDouble(getPrivateCreator(), getTag(), getValueRepresentation(), doubles);
    }

    interface Setter<B extends Builder<B, ?>, D extends DoubleDataElementMultiWrapper> extends org.dcm4che.typeddicom.Setter<B, D> {
        default B asDoubles(double... doubles) {
            getDataElementWrapper().setDoubles(doubles);
            return getBuilder();
        }
    }
}
