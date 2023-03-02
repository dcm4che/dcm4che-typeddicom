package org.dcm4che.typeddicom.parser.metamodel;

import org.davidmoten.text.utils.WordWrap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DataElementMetaInfo extends DataElementMetaInfoContainer {
    private static final String LINE_BREAK = "<br/>";
    private String tag;
    private String name;
    private String keyword;
    private String valueRepresentation;
    private String valueMultiplicity;
    private String comment;
    private boolean retired = false;
    private String retiredSince = "";
    private String tagConstant;

    private final Map<AdditionalAttributeInfo, Set<Context>> contextsOfAdditionalAttributeInfo = new HashMap<>();
    private String valueRepresentationWrapper;

    public DataElementMetaInfo() {
    }

    /**
     * Attention! Does not copy the subAttributes!
     *
     * @param other instance to copy fields from
     */
    public DataElementMetaInfo(DataElementMetaInfo other) {
        this.setTag(other.getTag());
        this.setName(other.getName());
        this.setKeyword(other.getKeyword());
        this.valueRepresentation = other.valueRepresentation;
        this.valueRepresentationWrapper = other.valueRepresentationWrapper;
        this.setValueMultiplicity(other.getValueMultiplicity());
        this.setComment(other.getComment());
        this.setRetired(other.isRetired());
        this.setRetiredSince(other.getRetiredSince());
        this.setTagConstant(other.getTagConstant());
    }

    public void addAdditionalAttributeInfoForContext(AdditionalAttributeInfo additionalAttributeInfo, Context context) {
        this.contextsOfAdditionalAttributeInfo
                .computeIfAbsent(additionalAttributeInfo, info -> new HashSet<>())
                .add(context);
    }

    public void setValueRepresentationWrapper(Map<String, ValueRepresentationMetaInfo> valueRepresentationsMap) {
        if (!valueRepresentation.equals("SQ")) {
            this.valueRepresentationWrapper = valueRepresentationsMap.get(valueRepresentation).keyword();
        } else {
            this.valueRepresentationWrapper = "Sequence";
        }
    }


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
        if (!contextsOfAdditionalAttributeInfo.isEmpty()) {
            html.append("<ul>");
            for (Map.Entry<AdditionalAttributeInfo, Set<Context>> additionalAttributeInfoSetEntry : contextsOfAdditionalAttributeInfo.entrySet()) {
                html.append("<li><strong>Described in the contexts:</strong><ul>");
                for (Context context : additionalAttributeInfoSetEntry.getValue()) {
                    html.append("<li>").append(context.getContextHTML()).append("</li>");
                }
                html.append("</ul>as follows: <br/>");
                AdditionalAttributeInfo additionalAttributeInfo = additionalAttributeInfoSetEntry.getKey();
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

    public String getTag() {
        return tag;
    }

    public String getName() {
        return name;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getValueRepresentation() {
        return valueRepresentation;
    }

    public String getValueMultiplicity() {
        return valueMultiplicity;
    }

    public String getComment() {
        return comment;
    }

    public boolean isRetired() {
        return retired;
    }

    public String getRetiredSince() {
        return retiredSince;
    }

    public String getTagConstant() {
        return tagConstant;
    }

    public Map<AdditionalAttributeInfo, Set<Context>> getContextsOfAdditionalAttributeInfo() {
        return contextsOfAdditionalAttributeInfo;
    }

    public String getValueRepresentationWrapper() {
        return valueRepresentationWrapper;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setValueRepresentation(String valueRepresentation) {
        this.valueRepresentation = valueRepresentation;
    }

    public void setValueMultiplicity(String valueMultiplicity) {
        this.valueMultiplicity = valueMultiplicity;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setRetired(boolean retired) {
        this.retired = retired;
    }

    public void setRetiredSince(String retiredSince) {
        this.retiredSince = retiredSince;
    }

    public void setTagConstant(String tagConstant) {
        this.tagConstant = tagConstant;
    }

    public void setValueRepresentationWrapper(String valueRepresentationWrapper) {
        this.valueRepresentationWrapper = valueRepresentationWrapper;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DataElementMetaInfo that = (DataElementMetaInfo) o;
        return retired == that.retired && Objects.equals(tag, that.tag) && Objects.equals(name, that.name) && Objects.equals(keyword, that.keyword) && Objects.equals(valueRepresentation, that.valueRepresentation) && Objects.equals(valueMultiplicity, that.valueMultiplicity) && Objects.equals(comment, that.comment) && Objects.equals(retiredSince, that.retiredSince) && Objects.equals(tagConstant, that.tagConstant) && Objects.equals(valueRepresentationWrapper, that.valueRepresentationWrapper);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tag, name, keyword, valueRepresentation, valueMultiplicity, comment, retired, retiredSince, tagConstant, valueRepresentationWrapper);
    }
}
