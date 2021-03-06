package org.dcm4che.typeddicom.valuerepresentations;

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
}
