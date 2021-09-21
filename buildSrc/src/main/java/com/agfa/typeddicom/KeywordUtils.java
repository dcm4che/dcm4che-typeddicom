package com.agfa.typeddicom;

import org.apache.commons.text.CaseUtils;

import java.util.regex.Pattern;

/**
 * TODO describe this class
 *
 * @author Niklas Roth  (niklasroth.@agfa.com)
 */
public class KeywordUtils {

    private KeywordUtils() {
    }

    public static String sanitizeAsJavaIdentifier(String name) {
        String identifier = name;
        identifier = identifier.replace("'", "");
        identifier = identifier.replace("(", "");
        identifier = identifier.replace(")", "");
        identifier = identifier.replace(" ", "");
        if (Character.isDigit(identifier.charAt(0))) {
            String[] numbersInText = {"Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine"};
            identifier = numbersInText[Integer.parseInt(identifier.substring(0, 1))] + identifier.substring(1);
        }
        identifier = CaseUtils.toCamelCase(identifier, true, ' ', '-', '/');
        if (!Pattern.matches("[A-Z][a-zA-Z\\d_$]*", identifier)) {
            throw new RuntimeException("Invalid class name " + identifier);
        }
        return identifier;
    }
}
