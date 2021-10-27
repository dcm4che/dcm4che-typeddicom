package com.agfa.typeddicom;

import org.dcm4che3.data.Sequence;

import java.util.AbstractList;
import java.util.Objects;

import static com.agfa.typeddicom.AttributesWrapper.wrap;
import static com.agfa.typeddicom.StringUtils.indent;

/**
 * TODO describe this class
 *
 * @author (. @ agfa.com)
 */
public class SequenceWrapper<T extends AbstractAttributesWrapper> extends AbstractList<T> {
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
        return wrap(sequence.get(index), itemClass);
    }

    @Override
    public int size() {
        return sequence.size();
    }

    @Override
    public T set(int index, T element) {
        return wrap(sequence.set(index, element.getAttributes()), itemClass);
    }

    @Override
    public void add(int index, T element) {
        sequence.add(index, element.getAttributes());
    }

    @Override
    public T remove(int index) {
        return wrap(sequence.remove(index), itemClass);
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
