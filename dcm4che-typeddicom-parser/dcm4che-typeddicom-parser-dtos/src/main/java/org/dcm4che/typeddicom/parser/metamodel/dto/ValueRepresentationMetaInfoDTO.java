package org.dcm4che.typeddicom.parser.metamodel.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonMerge;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ValueRepresentationMetaInfoDTO {
    private final String name;
    private final String tag;
    private final String definition;
    private final String characterRepertoire;
    private final String lengthOfValue;
    private final String href;
    @JsonMerge
    private final List<String> dataTypes;

    public ValueRepresentationMetaInfoDTO(
            @JsonProperty("name") String name,
            @JsonProperty("tag") String tag,
            @JsonProperty("definition") String definition,
            @JsonProperty("characterRepertoire") String characterRepertoire,
            @JsonProperty("lengthOfValue") String lengthOfValue,
            @JsonProperty("href") String href,
            @JsonProperty("dataTypes") List<String> dataTypes
    ) {
        this.name = name;
        this.tag = tag;
        this.definition = definition;
        this.characterRepertoire = characterRepertoire;
        this.lengthOfValue = lengthOfValue;
        this.href = href;
        this.dataTypes = dataTypes;
    }

    @JsonIgnore
    public String getImplementsInterfaces() {
        return Stream.concat(
                dataTypes.stream().map(s -> s + "Wrapper"),
                Stream.of("BytesDataElementWrapper")
        ).collect(Collectors.joining(", "));
    }

    @JsonIgnore
    public String getSetterImplementsInterfaces() {
        return Stream.concat(
                dataTypes.stream().map(s -> s + "Wrapper.Setter<B, D>"),
                Stream.of("BytesDataElementWrapper.Setter<B, D>")
        ).collect(Collectors.joining(", "));
    }

    @JsonIgnore
    public String getImplementsMultiInterfaces() {
        return Stream.concat(
                dataTypes.stream().map(s -> s + "MultiWrapper"),
                Stream.of("BytesDataElementWrapper")
        ).collect(Collectors.joining(", "));
    }

    @JsonIgnore
    public String getSetterImplementsMultiInterfaces() {
        return Stream.concat(
                dataTypes.stream().map(s -> s + "MultiWrapper.Setter<B, D>"),
                Stream.of("BytesDataElementWrapper.Setter<B, D>")
        ).collect(Collectors.joining(", "));
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public String getDefinition() {
        return definition;
    }

    public String getCharacterRepertoire() {
        return characterRepertoire;
    }

    public String getLengthOfValue() {
        return lengthOfValue;
    }

    public String getHref() {
        return href;
    }

    public List<String> getDataTypes() {
        return dataTypes;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ValueRepresentationMetaInfoDTO) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.tag, that.tag) &&
                Objects.equals(this.definition, that.definition) &&
                Objects.equals(this.characterRepertoire, that.characterRepertoire) &&
                Objects.equals(this.lengthOfValue, that.lengthOfValue) &&
                Objects.equals(this.href, that.href) &&
                Objects.equals(this.dataTypes, that.dataTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, tag, definition, characterRepertoire, lengthOfValue, href, dataTypes);
    }

    @Override
    public String toString() {
        return "ValueRepresentationMetaInfoDTO[" +
                "name=" + name + ", " +
                "tag=" + tag + ", " +
                "definition=" + definition + ", " +
                "characterRepertoire=" + characterRepertoire + ", " +
                "lengthOfValue=" + lengthOfValue + ", " +
                "href=" + href + ", " +
                "dataTypes=" + dataTypes + ']';
    }

}
