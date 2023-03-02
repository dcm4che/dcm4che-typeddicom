package org.dcm4che.typeddicom.parser.metamodel.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record ValueRepresentationMetaInfoDTO(
        String name,
        String tag,
        String definition,
        String characterRepertoire,
        String lengthOfValue,
        String href,
        List<String> dataTypes
) {
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
}
