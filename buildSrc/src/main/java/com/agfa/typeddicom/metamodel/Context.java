package com.agfa.typeddicom.metamodel;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class Context {
    private final List<ContextEntry> context;
    
    public Context(String rootContext, String href) {
        this(Collections.emptyList(), rootContext, href);
    }

    public Context(Context parentContext, String addedContext, String href) {
        this(parentContext.getContext(), addedContext, href);
    }

    private Context(List<ContextEntry> parentContext, String addedContext, String href) {
        context = new ArrayList<>(parentContext);
        context.add(new ContextEntry(addedContext, href));
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
}
