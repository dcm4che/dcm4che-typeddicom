package com.agfa.typeddicom;

import org.dcm4che3.data.Attributes;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.agfa.typeddicom.StringUtils.indent;


/**
 * This abstract class wraps dcm4che Attributes and provides accessors to it. It also implements {@link #equals} and
 * {@link #hashCode} which delegates to the wrapped Attributes and a toString() method which uses reflection to
 * provide a nice representation of the set and known Attributes.
 */
public abstract class AbstractAttributesWrapper implements AttributesWrapper {
    private final Attributes attributes;

    protected AbstractAttributesWrapper(Attributes attributes) {
        this.attributes = attributes;
    }

    protected AbstractAttributesWrapper() {
        this(new Attributes());
    }


    @Override
    public Attributes getAttributes() {
        return attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractAttributesWrapper that = (AbstractAttributesWrapper) o;
        return attributes.equals(that.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attributes);
    }

    @Override
    public String toString() {
        Map<String, String> keyValuePairs = Arrays.stream(this.getClass().getMethods())
                .filter(method -> method.getName().startsWith("contains"))
                .filter(method -> {
                    try {
                        return (boolean) method.invoke(this);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        return false;
                    }
                })
                .map(method -> method.getName().substring("contains".length()))
                .collect(Collectors.toMap(
                        keyword -> keyword,
                        keyword -> {
                            try {
                                return this.getClass().getMethod("get" + keyword).invoke(this).toString();
                            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                                return e.toString();
                            }
                        }));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getClass().getSimpleName()).append(':');
        for (Map.Entry<String, String> keyValuePair : keyValuePairs.entrySet()) {
            stringBuilder.append("\n").append(indent(keyValuePair.getValue(), 2));
        }
        return stringBuilder.toString();
    }
}
