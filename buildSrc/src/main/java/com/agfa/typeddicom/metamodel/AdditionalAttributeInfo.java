package com.agfa.typeddicom.metamodel;

import lombok.Data;

import java.io.Serializable;

@Data
public class AdditionalAttributeInfo implements Serializable {
    private final String name;
    private final String type;
    private final String attributeDescription;
}
