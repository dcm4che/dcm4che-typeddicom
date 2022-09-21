package org.dcm4che.typeddicom.valuerepresentations;

import org.dcm4che.typeddicom.AbstractDataElementWrapper;
import org.dcm4che.typeddicom.AbstractSetter;
import org.dcm4che.typeddicom.AttributesWrapper;
import org.dcm4che.typeddicom.Builder;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.VR;

public class UniversalDataElementWrapper extends AbstractDataElementWrapper implements
        BytesDataElementWrapper,
        DateDataElementWrapper,
        DateDataElementMultiWrapper,
        DoubleDataElementMultiWrapper,
        DoubleDataElementWrapper,
        FloatDataElementMultiWrapper,
        FloatDataElementWrapper,
        IntDataElementMultiWrapper,
        IntDataElementWrapper,
        StringDataElementMultiWrapper,
        StringDataElementWrapper {

    private final String privateCreator;
    private final int tag;
    private final VR valueRepresentation;

    public UniversalDataElementWrapper(Attributes attributes, String privateCreator, int tag, VR valueRepresentation) {
        super(attributes);
        this.privateCreator = privateCreator;
        this.tag = tag;
        this.valueRepresentation = valueRepresentation;
    }

    @Override
    public int getTag() {
        return tag;
    }

    @Override
    public VR getValueRepresentation() {
        return valueRepresentation;
    }
    
    @Override
    public String getPrivateCreator() {
        return privateCreator;
    }

    public static class Setter<B extends Builder<B, ? extends AttributesWrapper>> extends AbstractSetter<B, UniversalDataElementWrapper> implements
            BytesDataElementWrapper.Setter<B, UniversalDataElementWrapper>,
            DateDataElementWrapper.Setter<B, UniversalDataElementWrapper>,
            DateDataElementMultiWrapper.Setter<B, UniversalDataElementWrapper>,
            DoubleDataElementMultiWrapper.Setter<B, UniversalDataElementWrapper>,
            DoubleDataElementWrapper.Setter<B, UniversalDataElementWrapper>,
            FloatDataElementMultiWrapper.Setter<B, UniversalDataElementWrapper>,
            FloatDataElementWrapper.Setter<B, UniversalDataElementWrapper>,
            IntDataElementMultiWrapper.Setter<B, UniversalDataElementWrapper>,
            IntDataElementWrapper.Setter<B, UniversalDataElementWrapper>,
            StringDataElementMultiWrapper.Setter<B, UniversalDataElementWrapper>,
            StringDataElementWrapper.Setter<B, UniversalDataElementWrapper> {
        public Setter(B builder, UniversalDataElementWrapper dataElementWrapper) {
            super(builder, dataElementWrapper);
        }
    }
}
