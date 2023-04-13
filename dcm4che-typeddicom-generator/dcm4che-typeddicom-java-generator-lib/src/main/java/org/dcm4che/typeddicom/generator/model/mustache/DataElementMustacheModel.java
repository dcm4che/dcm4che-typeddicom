package org.dcm4che.typeddicom.generator.model.mustache;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.davidmoten.text.utils.WordWrap;
import org.dcm4che.typeddicom.parser.metamodel.dto.AdditionalAttributeInfoContextsDTO;
import org.dcm4che.typeddicom.parser.metamodel.dto.AdditionalAttributeInfoDTO;
import org.dcm4che.typeddicom.parser.metamodel.dto.DataElementMetaInfoDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.stream.Collectors;

public record DataElementMustacheModel(
        String keyword,
        String name,
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
    private static final String LINE_BREAK = "<br/>";

    public DataElementMustacheModel(String keyword, DataElementMetaInfoDTO dto) {
        this(
                keyword,
                dto.name(),
                dto.tag(),
                dto.tagConstant(),
                dto.valueRepresentation(),
                dto.valueMultiplicity(),
                dto.comment(),
                dto.retired(),
                dto.retiredSince(),
                dto.additionalAttributeInfo(),
                dto.contains()
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
                html.append("</ul>as follows: <br/>");
                AdditionalAttributeInfoDTO additionalAttributeInfo = additionalAttributeInfoSetEntry.additionalAttributeInfoDTO();
                html.append("<strong>Attribute Name:</strong> ").append(additionalAttributeInfo.name()).append(LINE_BREAK);
                html.append("<strong>Type:</strong> ").append(additionalAttributeInfo.type()).append(LINE_BREAK);
                html.append("<strong>Attribute Description:</strong> ")
                        .append(additionalAttributeInfo.attributeDescription()).append(LINE_BREAK);
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
        for (Element element : body.select("dd:empty, p:empty, dd:matchesOwn((?is) )")) {
            element.remove();
        }
    }

    private String javaDocify(String html, int indentationLevel) {
        String jdoc = WordWrap.from(html)
                .maxWidth(117 - indentationLevel)
                .extraWordChars("0123456789-._~:/?#[]@!$&'()*+,;%=\"<>")
                .newLine("\n * ")
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
}
