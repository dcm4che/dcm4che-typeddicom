package com.agfa.typeddicom.metamodel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * TODO describe this class
 *
 * @author Niklas Roth (niklas.roth@agfa.com)
 */
@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class DataElementMetaInfo extends DataElementMetaInfoContainer {
    private String tag;
    private String name;
    private String keyword;
    private String valueRepresentation;
    private String valueMultiplicity;
    private String comment;
    private boolean retired = false;
    private String retiredSince = "";
    private String tagConstant;

    @EqualsAndHashCode.Exclude
    private final Map<AdditionalAttributeInfo, Set<Context>> contextsOfAdditionalAttributeInfo = new HashMap<>();

    /**
     * Attention! Does not copy the subAttributes!
     *
     * @param other instance to copy fields from
     */
    public DataElementMetaInfo(DataElementMetaInfo other) {
        this.setTag(other.getTag());
        this.setName(other.getName());
        this.setKeyword(other.getKeyword());
        this.setValueRepresentation(other.getValueRepresentation());
        this.setValueMultiplicity(other.getValueMultiplicity());
        this.setComment(other.getComment());
        this.setRetired(other.isRetired());
        this.setRetiredSince(other.getRetiredSince());
        this.setTagConstant(other.getTagConstant());
    }

    public boolean isSequence() {
        return "SQ".equals(valueRepresentation);
    }
    
    public void addAdditionalAttributeInfoForContext(AdditionalAttributeInfo additionalAttributeInfo, Context context) {
        this.contextsOfAdditionalAttributeInfo
                .computeIfAbsent(additionalAttributeInfo, info -> new HashSet<>())
                .add(context);
    }
}
