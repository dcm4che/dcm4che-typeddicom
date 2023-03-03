package org.dcm4che.typeddicom;

import org.dcm4che3.data.Attributes;

public class UniversalAttributesWrapper extends AbstractAttributesWrapper {
    /**
     * @return a {@link UniversalBuilder} to create {{name}} Items with a fluent API.
     */
    public static UniversalBuilder builder() {
        return new UniversalBuilder();
    }

    /**
     * @param attributes The Attributes to be wrapped by the {@link UniversalBuilder}.
     *
     * @return a {@link UniversalBuilder} to create {{name}} Items with a fluent API. Instead of creating new Attributes it wraps the provided Attributes.
     */
    public static UniversalBuilder builder(Attributes attributes) {
        return new UniversalBuilder(attributes);
    }

    public UniversalAttributesWrapper(Attributes attributes) {
        super(attributes);
    }

    public static class UniversalBuilder extends AbstractAttributesWrapper implements Builder<UniversalBuilder, UniversalAttributesWrapper> {
        private UniversalBuilder() {
            super();
        }
    
        private UniversalBuilder(Attributes attributes) {
            super(attributes);
        }
    
        @Override
        public UniversalAttributesWrapper build() {
            return super.copy(UniversalAttributesWrapper.class);
        }
    }
}
