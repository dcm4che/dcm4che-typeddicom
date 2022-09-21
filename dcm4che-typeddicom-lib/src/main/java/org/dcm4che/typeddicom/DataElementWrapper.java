package org.dcm4che.typeddicom;

import org.dcm4che3.data.VR;

/**
 * Representation of a Data Element. It contains Attributes, the DICOM Tag and the Value Representation.
 * Accessors are using this data to query the values from the Attributes.
 */
public interface DataElementWrapper extends AttributesWrapper {
    /**
     * @return The <a href="https://dicom.nema.org/medical/dicom/current/output/chtml/part05/sect_6.2.html">DICOM Value 
     *         Representation (VR)</a> of the wrapped DICOM attributes.
     */
    VR getValueRepresentation();

    /**
     * @return The <a href="https://dicom.nema.org/medical/dicom/current/output/chtml/part06/chapter_6.html">DICOM
     *         Tag</a> of the wrapped DICOM attributes.
     */
    int getTag();

    /**
     * @return The <a href="https://dicom.nema.org/medical/dicom/current/output/chtml/part05/sect_7.8.html">DICOM
     *         Private Creator</a> of the wrapped DICOM attributes.
     */
    default String getPrivateCreator() {
        return null;
    }

    Object getValue();

    boolean exists();
}
