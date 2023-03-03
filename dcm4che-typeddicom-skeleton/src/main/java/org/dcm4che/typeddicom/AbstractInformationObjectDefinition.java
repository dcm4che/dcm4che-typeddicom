package org.dcm4che.typeddicom;

import org.dcm4che3.data.Attributes;

import java.util.Set;

public abstract class AbstractInformationObjectDefinition extends AbstractAttributesWrapper {

    public AbstractInformationObjectDefinition(Attributes attributes) {
        super(attributes);
    }

    public AbstractInformationObjectDefinition() {
        super();
    }

    /**
     *
     * @return The SOP Classes defined in
     * <a href="http://dicom.nema.org/medical/dicom/current/output/chtml/part04/sect_B.5.html">Part 4 - Section B.5 of the DICOM
     * Standard</a> not the one contained in the wrapped attributes. To get that one use
     * SOPClassUID.Holder#getSOPClassUID().
     */
    public abstract Set<SOPClass> getStandardSOPClasses();
}
