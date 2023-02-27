package org.dcm4che.typeddicom.generator.utils;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class KeywordUtils {

    private static final String[] tensNames = {
            "",
            " ten",
            " twenty",
            " thirty",
            " forty",
            " fifty",
            " sixty",
            " seventy",
            " eighty",
            " ninety"
    };
    private static final String[] numNames = {
            "",
            " one",
            " two",
            " three",
            " four",
            " five",
            " six",
            " seven",
            " eight",
            " nine",
            " ten",
            " eleven",
            " twelve",
            " thirteen",
            " fourteen",
            " fifteen",
            " sixteen",
            " seventeen",
            " eighteen",
            " nineteen"
    };

    private KeywordUtils() {
    }

    /**
     * Remove all invalid characters from a string which are not allowed in a Java class name. It removes "'", "(" and
     * ")" as well as " ", "-" and "/" while Capitalizing the following character to make it camel case. If the first
     * character is a digit it replaces it by its written out word.
     *
     * @param name the name with character which are not allowed in a class name
     * @return the sanitized name
     * @throws InvalidClassNameException if the resulting identifier does not comply to the Regex [A-Z][a-zA-Z\d_$]*.
     */
    public static String sanitizeAsJavaIdentifier(String name) throws InvalidClassNameException {
        String identifier = name;
        identifier = identifier.replace("'", "");
        identifier = identifier.replace("(", "");
        identifier = identifier.replace(")", "");
        StringBuilder numberPrefix = new StringBuilder();
        while (Character.isDigit(identifier.charAt(0))) {
            numberPrefix.append(identifier.charAt(0));
            identifier = identifier.substring(1);
        }
        if (numberPrefix.length() > 0) {
            identifier = convert(Long.parseLong(numberPrefix.toString())) + identifier;
        }
        identifier = Character.toUpperCase(identifier.charAt(0)) + identifier.substring(1);
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

    private static String convertLessThanOneThousand(int number) {
        String soFar;

        if (number % 100 < 20) {
            soFar = numNames[number % 100];
            number /= 100;
        } else {
            soFar = numNames[number % 10];
            number /= 10;

            soFar = tensNames[number % 10] + soFar;
            number /= 10;
        }
        if (number == 0) return soFar;
        return numNames[number] + " hundred" + soFar;
    }


    public static String convert(long number) {
        // 0 to 999 999 999 999
        if (number == 0) {
            return "zero";
        }

        String snumber = Long.toString(number);

        // pad with "0"
        String mask = "000000000000";
        DecimalFormat df = new DecimalFormat(mask);
        snumber = df.format(number);

        // XXXnnnnnnnnn
        int billions = Integer.parseInt(snumber.substring(0, 3));
        // nnnXXXnnnnnn
        int millions = Integer.parseInt(snumber.substring(3, 6));
        // nnnnnnXXXnnn
        int hundredThousands = Integer.parseInt(snumber.substring(6, 9));
        // nnnnnnnnnXXX
        int thousands = Integer.parseInt(snumber.substring(9, 12));

        String tradBillions;
        if (billions == 0) {
            tradBillions = "";
        } else {
            tradBillions = convertLessThanOneThousand(billions)
                    + " billion ";
        }
        String result = tradBillions;

        String tradMillions;
        if (millions == 0) {
            tradMillions = "";
        } else {
            tradMillions = convertLessThanOneThousand(millions)
                    + " million ";
        }
        result = result + tradMillions;

        String tradHundredThousands = switch (hundredThousands) {
            case 0 -> "";
            case 1 -> "one thousand ";
            default -> convertLessThanOneThousand(hundredThousands)
                    + " thousand ";
        };
        result = result + tradHundredThousands;

        String tradThousand;
        tradThousand = convertLessThanOneThousand(thousands);
        result = result + tradThousand;

        // remove extra spaces!
        return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
    }
}
