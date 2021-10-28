package com.agfa.typeddicom.utils;

import java.util.regex.Pattern;

public class KeywordUtils {

    private KeywordUtils() {
    }

    /**
     * Remove all invalid characters from a string which are not allowed in a Java class name. It removes "'", "(" and
     * ")" as well as " ", "-" and "/" while Capitalizing the following character to make it camel case. If the first
     * character is a digit it replaces it by its written out word.
     * @param name the name with character which are not allowed in a class name
     * @return the sanitized name
     * @throws InvalidClassNameException if the resulting identifier does not comply to the Regex [A-Z][a-zA-Z\d_$]*.
     */
    public static String sanitizeAsJavaIdentifier(String name) throws InvalidClassNameException {
        String identifier = name;
        identifier = identifier.replace("'", "");
        identifier = identifier.replace("(", "");
        identifier = identifier.replace(")", "");
        if (Character.isDigit(identifier.charAt(0))) {
            String[] numbersInText = {"Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine"};
            identifier = numbersInText[Integer.parseInt(identifier.substring(0, 1))] + identifier.substring(1);
        }
        identifier = removeCharactersAndCapitalizeFollowingLetter(identifier, ' ', '-', '/');
        if (!Pattern.matches("[A-Z][a-zA-Z\\d_$]*", identifier)) {
            throw new InvalidClassNameException("Invalid class name " + identifier);
        }
        return identifier;
    }

    private static String removeCharactersAndCapitalizeFollowingLetter(String identifier, char... chars) {
        for (char c : chars) {
            identifier = removeCharacterAndCapitalizeFollowingLetter(identifier, c);
        }
        return identifier;
    }

    private static String removeCharacterAndCapitalizeFollowingLetter(String identifier, char c) {
        identifier = removeCharacterFromStartOfString(identifier, c);
        int pos = identifier.indexOf(c);
        if (pos < 0) return identifier;
        return identifier.substring(0, pos) + removeCharacterAndCapitalizeFollowingLetter(Character.toUpperCase(identifier.charAt(pos + 1)) + identifier.substring(pos + 2), c);
    }

    private static String removeCharacterFromStartOfString(String identifier, char c) {
        if (identifier.charAt(0) == c) {
            return removeCharacterFromStartOfString(identifier.substring(1), c);
        } else {
            return identifier;
        }
    }
}
