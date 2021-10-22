package com.agfa.typeddicom;

class StringUtils {
    public static String indent(String text, int depth) {
        StringBuilder indentation = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            indentation.append(' ');
        }
        return indentation + text.replace("\n", "\n" + indentation);
    }
}
