package com.agfa.typeddicom.metamodel;

import lombok.*;

import java.io.Serializable;

/**
 * TODO describe this class
 *
 * @author (. @ agfa.com)
 */
@Data
@EqualsAndHashCode(callSuper=false)
@RequiredArgsConstructor
public class MacroMetaInfo extends DataElementMetaInfoContainer implements Serializable {
    private final String tableId;

}
