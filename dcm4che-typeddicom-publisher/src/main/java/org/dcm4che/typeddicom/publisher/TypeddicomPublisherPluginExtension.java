package org.dcm4che.typeddicom.publisher;

import org.gradle.api.provider.Property;

public interface TypeddicomPublisherPluginExtension {
    Property<String> getPomName();
    Property<String> getPomDescription();
}
