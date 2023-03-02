package org.dcm4che.typeddicom.parser.metamodel;

import java.util.Set;

public record ValueRepresentationMetaInfo(String tag, String name, String keyword, String definition,
                                          String characterRepertoire, String lengthOfValue,
                                          String href, Set<String> dataTypes) {
}
