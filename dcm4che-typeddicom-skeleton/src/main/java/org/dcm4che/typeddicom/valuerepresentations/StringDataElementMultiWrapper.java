package org.dcm4che.typeddicom.valuerepresentations;

import org.dcm4che.typeddicom.Builder;
import org.dcm4che.typeddicom.DataElementWrapper;

public interface StringDataElementMultiWrapper extends DataElementWrapper {
    default String[] getStrings() {
        return getAttributes().getStrings(getPrivateCreator(), getTag());
    }

    default String getString(int index) {
        return getAttributes().getString(getPrivateCreator(), getTag(), index);
    }

    default String getString(int index, String defaultValue) {
        return getAttributes().getString(getPrivateCreator(), getTag(), index, defaultValue);
    }

    default void setStrings(String... strings) {
        getAttributes().setString(getPrivateCreator(), getTag(), getValueRepresentation(), strings);
    }

    interface Setter<B extends Builder<B, ?>, D extends StringDataElementMultiWrapper> extends org.dcm4che.typeddicom.Setter<B, D> {
        default B asString(String... strings) {
            getDataElementWrapper().setStrings(strings);
            return getBuilder();
        }
    }
}
