package org.dcm4che.typeddicom;

public interface Setter<B extends AttributesWrapper, D extends DataElementWrapper> {
    B getBuilder();

    D getDataElementWrapper();
}
