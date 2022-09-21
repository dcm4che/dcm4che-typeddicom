package org.dcm4che.typeddicom;

import org.dcm4che.typeddicom.valuerepresentations.UniversalDataElementWrapper;
import org.dcm4che3.data.Sequence;
import org.dcm4che3.data.VR;

public interface Builder<SELF extends Builder<SELF, T>, T extends AttributesWrapper> extends AttributesWrapper {
    T build();
    
    default UniversalDataElementWrapper.Setter<SELF> setAttribute(String privateCreator, int tag, VR valueRepresentation) {
        return new UniversalDataElementWrapper.Setter<>((SELF) this, new UniversalDataElementWrapper(this.getAttributes(), privateCreator, tag, valueRepresentation));
    }
    
    default SELF setSequence(String privateCreator, int tag, Builder<?, ?>... itemBuilders) {
        this.getAttributes().remove(tag);
        Sequence sequence = this.getAttributes().newSequence(privateCreator, tag, itemBuilders.length);
        for (Builder<?, ?> builder : itemBuilders) {
            sequence.add(builder.build().getAttributes());
        }
        return (SELF) this;
    }
}
