package org.dcm4che.typeddicom.metamodel;

/**
 * TODO describe this class
 *
 * @author Niklas Roth  (niklas.roth.@agfa.com)
 */
public record ValueRepresentationMetaInfo(String tag, String name, String keyword, String definition,
                                          String characterRepertoire, String lengthOfValue,
                                          String href, String implementsInterfaces) {
}
