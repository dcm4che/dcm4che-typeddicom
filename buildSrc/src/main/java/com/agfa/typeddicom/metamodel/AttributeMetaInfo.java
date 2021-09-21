package com.agfa.typeddicom.metamodel;

import lombok.Data;

import java.io.Serializable;

/**
 * TODO describe this class
 *
 * @author (. @ agfa.com)
 */
@Data
public class AttributeMetaInfo implements Serializable {
    private String name;
    private String attributeDescription;
    private String type;
    private DataElementMetaInfo dataElementMetaInfo;

    public AttributeMetaInfo(String name, DataElementMetaInfo dataElementMetaInfo, String attributeDescription) {
        this(name, dataElementMetaInfo, "", attributeDescription);
    }

    public AttributeMetaInfo(String name, DataElementMetaInfo dataElementMetaInfo, String type, String attributeDescription) {
        this.name = name;
        this.type = type;
        this.dataElementMetaInfo = dataElementMetaInfo;
        this.attributeDescription = attributeDescription;
    }
}
