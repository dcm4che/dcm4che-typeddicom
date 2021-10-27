package com.agfa.typeddicom.metamodel;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO describe this class
 *
 * @author (. @ agfa.com)
 */
public class DataElementMetaInfoContainer implements Serializable {
    private final List<DataElementMetaInfo> subDataElementMetaInfos = new ArrayList<>();

    public boolean addDataElementMetaInfo(DataElementMetaInfo attributeMetaInfo) {
        for (DataElementMetaInfo subAttributeMetaInfo : subDataElementMetaInfos) {
            if (subAttributeMetaInfo.equals(attributeMetaInfo)) {
                return false;
            }
        }
        return this.subDataElementMetaInfos.add(attributeMetaInfo);
    }

    public boolean addDataElementMetaInfo(Iterable<DataElementMetaInfo> attributeMetaInfos) {
        boolean changed = false;
        for (DataElementMetaInfo attributeMetaInfo : attributeMetaInfos) {
            changed |= this.addDataElementMetaInfo(attributeMetaInfo);
        }
        return changed;
    }

    public Iterable<DataElementMetaInfo> getSubDataElementMetaInfos() {
        return subDataElementMetaInfos;
    }

    public String implementsHolderInterfaces() {
        return subDataElementMetaInfos.stream().map(dataElementMetaInfo -> dataElementMetaInfo.getKeyword() + ".Holder").collect(Collectors.joining(", "));
    }
}
