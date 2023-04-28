package org.dcm4che.typeddicom.generator;

import org.davidmoten.text.utils.WordWrap;

public class JavaDocUtils {
    private static final String JAVA_DOC_NEWLINE = "\n * ";
    private static final int MAX_LINE_LENGTH = 120;
    private static final String ZERO_WIDTH_CHARACTERS_REGEX = "[\n\r]";

    private JavaDocUtils() {
    }

    public static String javaDocify(String html, int indentationLevel) {
        String jdoc = WordWrap.from(html)
                .maxWidth(MAX_LINE_LENGTH
                        - JAVA_DOC_NEWLINE.replaceAll(ZERO_WIDTH_CHARACTERS_REGEX, "").length()
                        - indentationLevel)
                .extraWordChars("0123456789-._~:/?#[]@!$&'()*+,;%=\"<>")
                .newLine(JAVA_DOC_NEWLINE)
                .breakWords(false)
                .wrap();
        jdoc = "/**\n * " + jdoc + "\n */";
        jdoc = indent(jdoc, indentationLevel);
        return jdoc;
    }

    private static String indent(String text, int indentationLevel) {
        String indent = " ".repeat(indentationLevel * 4);
        return indent + text.replace("\n", "\n" + indent);
    }
}
