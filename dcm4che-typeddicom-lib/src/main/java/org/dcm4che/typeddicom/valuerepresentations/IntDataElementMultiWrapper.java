package org.dcm4che.typeddicom.valuerepresentations;

import org.dcm4che.typeddicom.Builder;
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

    interface Setter<B extends Builder<B, ?>, D extends IntDataElementMultiWrapper> extends Builder.Setter<B, D> {
        default B asInts(int... ints) {
            getDataElementWrapper().setInts(ints);
            return getBuilder();
        }
    }
}
