package org.dcm4che.typeddicom.valuerepresentations;

import org.dcm4che.typeddicom.AttributesWrapper;
import org.dcm4che.typeddicom.DataElementWrapper;

public interface IntDataElementMultiWrapper extends DataElementWrapper {
    default int[] getInts() {
        return getAttributes().getInts(getTag());
    }
    
    default int getInt(int index, int defaultValue) {
        return getAttributes().getInt(getTag(), index, defaultValue);
    }
    
    default void setInts(int... ints) {
        getAttributes().setInt(getTag(), getValueRepresentation(), ints);
    }

    interface Setter<D extends IntDataElementMultiWrapper, P extends AttributesWrapper> extends DataElementWrapper.Setter<D, P> {
        default P setInts(int... ints) {
            getDataElementWrapper().setInts(ints);
            return getParentAttributesWrapper();
        }
    }
}
