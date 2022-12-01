package org.dcm4che.typeddicom;

import org.dcm4che3.data.Sequence;
import org.dcm4che3.data.VR;

import java.util.AbstractList;
import java.util.Objects;

import static org.dcm4che.typeddicom.StringUtils.indent;

/**
 * This abstract class implements all list methods for Sequences.
 */
public abstract class SequenceWrapper<SELF extends SequenceWrapper<SELF, T>, T extends AbstractSequenceItemWrapper> extends AbstractList<T> {
    public static final VR VALUE_REPRESENTATION = VR.SQ;

    private final Sequence sequence;
    private final Class<T> itemClass;

    protected SequenceWrapper(Sequence sequence, Class<T> itemClass) {
        this.sequence = sequence;
        this.itemClass = itemClass;
    }

    public Sequence getSequence() {
        return sequence;
    }

    /**
     * @return The <a href="https://dicom.nema.org/medical/dicom/current/output/chtml/part05/sect_6.2.html">DICOM Value 
     *         Representation (VR)</a> of the wrapped DICOM sequence.
     */
    public VR getValueRepresentation() {
        return VALUE_REPRESENTATION;
    }

    /**
     * @return The <a href="https://dicom.nema.org/medical/dicom/current/output/chtml/part05/sect_7.8.html">DICOM
     *         Private Creator</a> of the wrapped DICOM attributes. null if not private
     */
    public abstract String getPrivateCreator();

    /**
     * @return The <a href="https://dicom.nema.org/medical/dicom/current/output/chtml/part06/chapter_6.html">DICOM 
     *         Tag</a> of the wrapped DICOM sequence.
     */
    public abstract int getTag();
    
    @Override
    public T get(int index) {
        return AttributesWrapper.wrap(sequence.get(index), itemClass);
    }

    @Override
    public int size() {
        return sequence.size();
    }

    @Override
    public T set(int index, T element) {
        return AttributesWrapper.wrap(sequence.set(index, element.getAttributes()), itemClass);
    }

    @Override
    public void add(int index, T element) {
        sequence.add(index, element.getAttributes());
    }

    @Override
    public T remove(int index) {
        return AttributesWrapper.wrap(sequence.remove(index), itemClass);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SequenceWrapper<SELF, ?> that = (SequenceWrapper<SELF, ?>) o;
        return sequence.equals(that.sequence) && itemClass.equals(that.itemClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sequence, itemClass);
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append(this.getClass().getSimpleName()).append(':');
        for (int i = 0; i < size(); i++) {
            string.append('\n').append(indent("[" + (i + 1) + "] " + get(i).toString(), 2));
        }
        return string.toString();
    }
}
