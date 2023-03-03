package org.dcm4che.typeddicom;

public abstract class AbstractSetter<B extends AttributesWrapper, D extends DataElementWrapper> implements Setter<B, D> {
    private final B builder;
    private final D dataElementWrapper;

    protected AbstractSetter(B builder, D dataElementWrapper) {
        this.builder = builder;
        this.dataElementWrapper = dataElementWrapper;
    }

    @Override
    public B getBuilder() {
        return builder;
    }

    @Override
    public D getDataElementWrapper() {
        return dataElementWrapper;
    }
}
