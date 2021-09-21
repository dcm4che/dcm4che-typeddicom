package com.agfa.typeddicom.metamodel;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO describe this class
 *
 * @author (. @ agfa.com)
 */
@Data
@RequiredArgsConstructor
public class MacroMetaInfo implements Serializable {
    private final String tableId;
    private final List<AttributeMetaInfo> attributeMetaInfos = new ArrayList<>();
}
