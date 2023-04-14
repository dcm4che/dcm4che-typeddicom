package org.dcm4che.typeddicom.generator.model.mustache;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.davidmoten.text.utils.WordWrap;
import org.dcm4che.typeddicom.parser.metamodel.dto.AdditionalAttributeInfoContextsDTO;
import org.dcm4che.typeddicom.parser.metamodel.dto.AdditionalAttributeInfoDTO;
import org.dcm4che.typeddicom.parser.metamodel.dto.DataElementMetaInfoDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class DataElementMustacheModel {
    private static final String LINE_BREAK = "<br>";
    private static final String JAVA_DOC_NEWLINE = "\n * ";
    private static final int MAX_LINE_LENGTH = 120;
    public static final String ZERO_WIDTH_CHARACTERS_REGEX = "[\n\r]";
    private final String keyword;
    private final String name;
    private final String privateCreatorConstant;
    private final String tag;
    private final String tagConstant;
    private final String valueRepresentation;
    private final String valueMultiplicity;
    private final String comment;
    private final boolean retired;
    private final String retiredSince;
    private final List<AdditionalAttributeInfoContextsDTO> additionalAttributeInfo;
    private final List<String> contains;

    public DataElementMustacheModel(
            String keyword,
            String name,
            String privateCreatorConstant,
            String tag,
            String tagConstant,
            String valueRepresentation,
            String valueMultiplicity,
            String comment,
            boolean retired,
            String retiredSince,
            List<AdditionalAttributeInfoContextsDTO> additionalAttributeInfo,
            List<String> contains
    ) {
        this.keyword = keyword;
        this.name = name;
        this.privateCreatorConstant = privateCreatorConstant;
        this.tag = tag;
        this.tagConstant = tagConstant;
        this.valueRepresentation = valueRepresentation;
        this.valueMultiplicity = valueMultiplicity;
        this.comment = comment;
        this.retired = retired;
        this.retiredSince = retiredSince;
        this.additionalAttributeInfo = additionalAttributeInfo;
        this.contains = contains;
    }

    public DataElementMustacheModel(String keyword, DataElementMetaInfoDTO dto) {
        this(
                keyword,
                dto.getName(),
                dto.getPrivateCreatorConstant(),
                dto.getTag(),
                dto.getTagConstant(),
                dto.getValueRepresentation(),
                dto.getValueMultiplicity(),
                dto.getComment(),
                dto.getRetired(),
                dto.getRetiredSince(),
                dto.getAdditionalAttributeInfo(),
                dto.getContains()
        );
    }

    @JsonIgnore
    public boolean isSequence() {
        return valueRepresentation.equals("Sequence");
    }

    @JsonIgnore
    public String getValueRepresentationWrapper() {
        if (valueMultiplicity().equals("1")) {
            return valueRepresentation + "Wrapper";
        } else {
            return valueRepresentation + "MultiWrapper";
        }
    }

    @JsonIgnore
    public String implementsBuilderInterfaces() {
        return contains.stream()
                .map(key -> key + ".Builder<Builder, Item>")
                .collect(Collectors.joining(", "));
    }

    @JsonIgnore
    public String implementsHolderInterfaces() {
        return contains.stream().map(key -> key + ".Holder").collect(Collectors.joining(", "));
    }

    @JsonIgnore
    public String classJavaDoc() {
        StringBuilder htmlBuilder = new StringBuilder();
        appendGeneralInfo(htmlBuilder);
        appendContextInfo(htmlBuilder);
        Element body = Jsoup.parse(htmlBuilder.toString()).body();
        sanitizeJDocHtml(body);
        String htmlString = body.html();
        htmlString = htmlString.replace("\n<br>", "<br>\n");
        if (retired) {
            htmlString += "\n\n@deprecated ";
            htmlString += comment;
        }
        return javaDocify(htmlString, 0);
    }

    private void appendGeneralInfo(StringBuilder html) {
        html.append("<strong>Name:</strong> ").append(name).append(LINE_BREAK);
        html.append("<strong>Keyword:</strong> ").append(keyword).append(LINE_BREAK);
        html.append("<strong>Tag:</strong> ").append(tag).append(LINE_BREAK);
        html.append("<strong>Value Representation:</strong> ").append(valueRepresentation).append(LINE_BREAK);
        html.append("<strong>Value Multiplicity:</strong> ").append(valueMultiplicity).append(LINE_BREAK);
        if (comment.length() > 0) {
            html.append("<strong>Comment:</strong> ").append(comment).append(LINE_BREAK);
        }
    }

    private void appendContextInfo(StringBuilder html) {
        if (!additionalAttributeInfo.isEmpty()) {
            html.append("<ul>");
            for (AdditionalAttributeInfoContextsDTO additionalAttributeInfoSetEntry : additionalAttributeInfo) {
                html.append("<li><strong>Described in the contexts:</strong><ul>");
                html.append(additionalAttributeInfoSetEntry.getContextsHTML());
                html.append("</ul>as follows: <br>");
                AdditionalAttributeInfoDTO additionalAttributeInfo = additionalAttributeInfoSetEntry.getAdditionalAttributeInfoDTO();
                html.append("<strong>Attribute Name:</strong> ").append(additionalAttributeInfo.getName()).append(LINE_BREAK);
                html.append("<strong>Type:</strong> ").append(additionalAttributeInfo.getType()).append(LINE_BREAK);
                html.append("<strong>Attribute Description:</strong> ")
                        .append(additionalAttributeInfo.getAttributeDescription()).append(LINE_BREAK);
                html.append("</li>");
            }
            html.append("</ul>");
        }
    }

    private void sanitizeJDocHtml(Element body) {
        for (Element element : body.select("dl > p")) {
            assert element.parent() != null; // should always have a parent
            element.parent().before(element);
        }
        for (Element element : body.select("p:empty")) {
            element.remove();
        }
        for (Element element : body.select("dd:empty, dd:matchesOwn((?is))")) {
            element.remove();
        }
    }

    private String javaDocify(String html, int indentationLevel) {
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

    private String indent(String text, int indentationLevel) {
        String indent = " ".repeat(indentationLevel * 4);
        return indent + text.replace("\n", "\n" + indent);
    }

    public String keyword() {
        return keyword;
    }

    public String name() {
        return name;
    }

    public String privateCreatorConstant() {
        return privateCreatorConstant;
    }

    public String tag() {
        return tag;
    }

    public String tagConstant() {
        return tagConstant;
    }

    public String valueRepresentation() {
        return valueRepresentation;
    }

    public String valueMultiplicity() {
        return valueMultiplicity;
    }

    public String comment() {
        return comment;
    }

    public boolean retired() {
        return retired;
    }

    public String retiredSince() {
        return retiredSince;
    }

    public List<AdditionalAttributeInfoContextsDTO> additionalAttributeInfo() {
        return additionalAttributeInfo;
    }

    public List<String> contains() {
        return contains;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (DataElementMustacheModel) obj;
        return Objects.equals(this.keyword, that.keyword) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.privateCreatorConstant, that.privateCreatorConstant) &&
                Objects.equals(this.tag, that.tag) &&
                Objects.equals(this.tagConstant, that.tagConstant) &&
                Objects.equals(this.valueRepresentation, that.valueRepresentation) &&
                Objects.equals(this.valueMultiplicity, that.valueMultiplicity) &&
                Objects.equals(this.comment, that.comment) &&
                this.retired == that.retired &&
                Objects.equals(this.retiredSince, that.retiredSince) &&
                Objects.equals(this.additionalAttributeInfo, that.additionalAttributeInfo) &&
                Objects.equals(this.contains, that.contains);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyword, name, privateCreatorConstant, tag, tagConstant, valueRepresentation, valueMultiplicity, comment, retired, retiredSince, additionalAttributeInfo, contains);
    }

    @Override
    public String toString() {
        return "DataElementMustacheModel[" +
                "keyword=" + keyword + ", " +
                "name=" + name + ", " +
                "privateCreatorConstant=" + privateCreatorConstant + ", " +
                "tag=" + tag + ", " +
                "tagConstant=" + tagConstant + ", " +
                "valueRepresentation=" + valueRepresentation + ", " +
                "valueMultiplicity=" + valueMultiplicity + ", " +
                "comment=" + comment + ", " +
                "retired=" + retired + ", " +
                "retiredSince=" + retiredSince + ", " +
                "additionalAttributeInfo=" + additionalAttributeInfo + ", " +
                "contains=" + contains + ']';
    }

}
