package org.dcm4che.typeddicom.valuerepresentations;

import org.dcm4che.typeddicom.Builder;
import org.dcm4che.typeddicom.DataElementWrapper;

public interface StringDataElementWrapper extends DataElementWrapper {
    default String getString() {
        return getAttributes().getString(getPrivateCreator(), getTag());
    }

    default void setString(String string) {
        getAttributes().setString(getPrivateCreator(), getTag(), getValueRepresentation(), string);
    }

    interface Setter<B extends Builder<B, ?>, D extends StringDataElementWrapper> extends org.dcm4che.typeddicom.Setter<B, D> {
        default B asString(String string) {
            getDataElementWrapper().setString(string);
            return getBuilder();
        }
    }
}
