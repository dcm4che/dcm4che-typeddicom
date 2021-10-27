package com.agfa.typeddicom.metamodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    
    public String getContextHTML() {
        StringBuilder html = new StringBuilder();
        for (int i = 0; i < this.context.size(); i++) {
            html.append("<a href=\"");
            html.append(this.context.get(i).href());
            html.append("\">");
            html.append(this.context.get(i).contextName());
            html.append("</a>");
            if (i < this.context.size() - 1 ) {
                html.append(" &gt; ");
            }
        }
        return html.toString();
    }

    public List<ContextEntry> getContext() {
        return context;
    }

    @Override
    public String toString() {
        return context.stream().map(ContextEntry::contextName).collect(Collectors.joining(" > "));
    }
}
