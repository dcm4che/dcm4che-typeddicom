package org.dcm4che.typeddicom.metamodel;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * This abstract class holds a List of DataElementMetaInfo objects. It should be extended by classes which need to hold
 * multiple DataElementMetaInfo objects
 */
public abstract class DataElementMetaInfoContainer implements Serializable {
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
        return StreamSupport.stream(getSubDataElementMetaInfos().spliterator(), false)
                .map(dataElementMetaInfo -> dataElementMetaInfo.getKeyword() + ".Holder")
                .collect(Collectors.joining(", "));
    }

    public abstract String implementsBuilderInterfaces();
}
