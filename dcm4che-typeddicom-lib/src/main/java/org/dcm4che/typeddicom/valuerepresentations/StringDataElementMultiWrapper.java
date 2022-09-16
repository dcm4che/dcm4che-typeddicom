package org.dcm4che.typeddicom.valuerepresentations;

import org.dcm4che.typeddicom.Builder;
import org.dcm4che.typeddicom.DataElementWrapper;

public interface StringDataElementMultiWrapper extends DataElementWrapper {
    default String[] getStrings() {
        return getAttributes().getStrings(getTag());
    }

    default String getString(int index) {
        return getAttributes().getString(getTag(), index);
    }

    default String getString(int index, String defaultValue) {
        return getAttributes().getString(getTag(), index, defaultValue);
    }

    default void setStrings(String... strings) {
        getAttributes().setString(getTag(), getValueRepresentation(), strings);
    }

    interface Setter<B extends Builder<B, ?>, D extends StringDataElementMultiWrapper> extends Builder.Setter<B, D> {
        default B asString(String... strings) {
            getDataElementWrapper().setStrings(strings);
            return getBuilder();
        }
    }
}
