package org.dcm4che.typeddicom.metamodel;

public record ValueRepresentationMetaInfo(String tag, String name, String keyword, String definition,
                                          String characterRepertoire, String lengthOfValue,
                                          String href, String implementsInterfaces, String setterImplementsInterfaces) {
}
