package com.agfa.typeddicom;

import com.agfa.typeddicom.valuerepresentations.*;
import org.dcm4che3.data.Attributes;

import java.util.Arrays;
import java.util.Objects;

import static com.agfa.typeddicom.StringUtils.indent;

/**
 * TODO describe this class
 *
 * @author Niklas Roth (niklas.roth@agfa.com)
 */
abstract public class AbstractDataElementWrapper extends AbstractAttributesWrapper implements DataElementWrapper {

    protected AbstractDataElementWrapper(Attributes attributes) {
        super(attributes);
    }

    protected AbstractDataElementWrapper() {
        super();
    }

    @Override
    public Object getValue() {
        return getAttributes().getValue(getTag());
    }

    @Override
    public boolean exists() {
        return getAttributes().contains(getTag());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractDataElementWrapper that = (AbstractDataElementWrapper) o;
        if (getTag() != that.getTag()) {
            return false;
        }
        if (getValueRepresentation() != that.getValueRepresentation()) {
            return false;
        }
        if (getValue() instanceof boolean[]) {
            return Arrays.equals((boolean[]) getValue(), (boolean[]) that.getValue());
        } else if (getValue() instanceof byte[]) {
            return Arrays.equals((byte[]) getValue(), (byte[]) that.getValue());
        } else if (getValue() instanceof short[]) {
            return Arrays.equals((short[]) getValue(), (short[]) that.getValue());
        } else if (getValue() instanceof char[]) {
            return Arrays.equals((char[]) getValue(), (char[]) that.getValue());
        } else if (getValue() instanceof int[]) {
            return Arrays.equals((int[]) getValue(), (int[]) that.getValue());
        } else if (getValue() instanceof long[]) {
            return Arrays.equals((long[]) getValue(), (long[]) that.getValue());
        } else if (getValue() instanceof float[]) {
            return Arrays.equals((float[]) getValue(), (float[]) that.getValue());
        } else if (getValue() instanceof double[]) {
            return Arrays.equals((double[]) getValue(), (double[]) that.getValue());
        } else if (getValue() instanceof Object[]) {
            return Arrays.equals((Object[]) getValue(), (Object[]) that.getValue());
        } else {
            return getValue().equals(that.getValue());
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTag(), getValueRepresentation(), getValue());
    }

    @Override
    public String toString() {
        String string;
        if (this instanceof StringDataElementWrapper) {
            string = ((StringDataElementWrapper) this).getString();
        } else if (this instanceof StringDataElementMultiWrapper) {
            string = Arrays.toString(((StringDataElementMultiWrapper) this).getStrings());
        } else if (this instanceof DateDataElementWrapper) {
            string = ((DateDataElementWrapper) this).getDate().toString();
        } else if (this instanceof DateDataElementMultiWrapper) {
            string = Arrays.toString(((DateDataElementMultiWrapper) this).getDates());
        } else if (this instanceof DoubleDataElementWrapper) {
            string = Double.toString(((DoubleDataElementWrapper) this).getDouble(0d));
        } else if (this instanceof DoubleDataElementMultiWrapper) {
            string = Arrays.toString(((DoubleDataElementMultiWrapper) this).getDoubles());
        } else if (this instanceof FloatDataElementWrapper) {
            string = Float.toString(((FloatDataElementWrapper) this).getFloat(0f));
        } else if (this instanceof FloatDataElementMultiWrapper) {
            string = Arrays.toString(((FloatDataElementMultiWrapper) this).getFloats());
        } else if (this instanceof IntDataElementWrapper) {
            string = Integer.toString(((IntDataElementWrapper) this).getInt(0));
        } else if (this instanceof IntDataElementMultiWrapper) {
            string = Arrays.toString(((IntDataElementMultiWrapper) this).getInts());
        } else if (this instanceof BytesDataElementWrapper) {
            string = Arrays.toString(((BytesDataElementWrapper) this).getSafeBytes());
        } else {
            string = "!!Uninterpretable value!!";
        }
        if (string != null && string.contains("\n")) {
            string = this.getClass().getSimpleName() + ":\n" + indent(string, 2);
        } else {
            string = this.getClass().getSimpleName() + ": " + string;
        }
        return string;
    }
}
