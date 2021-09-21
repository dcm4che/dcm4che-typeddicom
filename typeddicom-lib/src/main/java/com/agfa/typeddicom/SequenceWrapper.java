package com.agfa.typeddicom;

import org.dcm4che3.data.Sequence;

import java.util.AbstractList;
import java.util.Objects;

/**
 * TODO describe this class
 *
 * @author (. @ agfa.com)
 */
public class SequenceWrapper<T extends AttributesWrapper> extends AbstractList<T> {
    private final Sequence sequence;
    private final Class<T> itemClass;

    public SequenceWrapper(Sequence sequence, Class<T> itemClass) {
        this.sequence = sequence;
        this.itemClass = itemClass;
    }

    public Sequence getSequence() {
        return sequence;
    }

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
        SequenceWrapper<?> that = (SequenceWrapper<?>) o;
        return sequence.equals(that.sequence) && itemClass.equals(that.itemClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sequence, itemClass);
    }
}
