package com.agfa.typeddicom.metamodel;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO describe this class
 *
 * @author (. @ agfa.com)
 */
@Data
public class DataElementMetaInfoContainer implements Serializable {
    @Setter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
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
}
