package org.dcm4che.typeddicom.valuerepresentations;

import org.dcm4che.typeddicom.AttributesWrapper;
import org.dcm4che.typeddicom.DataElementWrapper;

public interface StringDataElementWrapper extends DataElementWrapper {
    default String getString() {
        return getAttributes().getString(getTag());
    }

    default void setString(String string) {
        getAttributes().setString(getTag(), getValueRepresentation(), string);
    }

    interface Setter<D extends StringDataElementWrapper, P extends AttributesWrapper> extends DataElementWrapper.Setter<D, P> {
        default P setString(String string) {
            getDataElementWrapper().setString(string);
            return getParentAttributesWrapper();
        }
    }
}
