package org.dcm4che.typeddicom;

class StringUtils {
    /**
     * Indents the text with the specified depth.
     * @param text The text to be indented
     * @param depth The number of whitespaces added to the beginning of each line
     * @return the indented text
     */
    public static String indent(String text, int depth) {
        StringBuilder indentation = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            indentation.append(' ');
        }
        return indentation + text.replace("\n", "\n" + indentation);
    }
}
