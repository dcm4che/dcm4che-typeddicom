package org.dcm4che.typeddicom.valuerepresentations;

import org.dcm4che.typeddicom.Builder;
import org.dcm4che.typeddicom.DataElementWrapper;

import java.io.IOException;

public interface BytesDataElementWrapper extends DataElementWrapper {
    default byte[] getBytes() throws IOException {
        return getAttributes().getBytes(getTag());
    }

    default byte[] getSafeBytes() {
        return getAttributes().getSafeBytes(getTag());
    }

    default void setBytes(byte[] bytes) {
        getAttributes().setBytes(getTag(), getValueRepresentation(), bytes);
    }
    
    interface Setter<B extends Builder<B, ?>, D extends BytesDataElementWrapper> extends Builder.Setter<B, D> {
        default B asBytes(byte[] bytes) {
            getDataElementWrapper().setBytes(bytes);
            return getBuilder();
        }
    }
}
