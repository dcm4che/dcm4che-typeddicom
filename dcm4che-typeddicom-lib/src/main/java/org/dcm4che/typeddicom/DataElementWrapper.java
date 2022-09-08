package org.dcm4che.typeddicom;

import org.dcm4che3.data.VR;

/**
 * Representation of a Data Element. It contains Attributes, the DICOM Tag and the Value Representation.
 * Accessors are using this data to query the values from the Attributes.
 */
public interface DataElementWrapper extends AttributesWrapper {
    /**
     * @return The <a href="https://dicom.nema.org/medical/dicom/current/output/chtml/part05/sect_6.2.html">DICOM Value 
     *         Representation (VR)</a> of the wrapped DICOM sequence.
     */
    VR getValueRepresentation();

    /**
     * @return The <a href="https://dicom.nema.org/medical/dicom/current/output/chtml/part06/chapter_6.html">DICOM
     *         Tag</a> of the wrapped DICOM sequence.
     */
    int getTag();

    Object getValue();

    boolean exists();

    interface Setter<D extends DataElementWrapper, P extends AttributesWrapper> {
        D getDataElementWrapper();

        P getParentAttributesWrapper();
    }

    abstract class AbstractSetter<D extends DataElementWrapper, P extends AttributesWrapper> implements Setter<D, P> {
        private final D dataElementWrapper;
        private final P parentAttributesWrapper;

        protected AbstractSetter(D dataElementWrapper, P parentAttributesWrapper) {
            this.dataElementWrapper = dataElementWrapper;
            this.parentAttributesWrapper = parentAttributesWrapper;
        }

        @Override
        public D getDataElementWrapper() {
            return dataElementWrapper;
        }
        
        @Override
        public P getParentAttributesWrapper() {
            return parentAttributesWrapper;
        }
    }
}
