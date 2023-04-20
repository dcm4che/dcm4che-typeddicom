package org.dcm4che.typeddicom.parser.metamodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Context {
    private final List<ContextEntry> context;

    public Context(String rootContext, String href) {
        this(new ContextEntry(rootContext, href));
    }

    public Context(ContextEntry rootContext) {
        this(Collections.emptyList(), rootContext);
    }

    public Context(Context parentContext, String addedContext, String href) {
        this(parentContext, new ContextEntry(addedContext, href));
    }

    public Context(Context parentContext, ContextEntry addedContextEntry) {
        this(parentContext.getContext(), addedContextEntry);
    }

    private Context(List<ContextEntry> parentContext, ContextEntry addedContextEntry) {
        context = new ArrayList<>(parentContext);
        context.add(addedContextEntry);
    }
    

    public List<ContextEntry> getContext() {
        return context;
    }

    @Override
    public String toString() {
        return context.stream().map(ContextEntry::contextName).collect(Collectors.joining(" > "));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Context context1 = (Context) o;
        return Objects.equals(context, context1.context);
    }

    @Override
    public int hashCode() {
        return Objects.hash(context);
    }
}
