package com.agfa.typeddicom.table;

import lombok.Data;

/**
 * TODO describe this class
 *
 * @author (. @ agfa.com)
 */
@Data
public class TableCell {
    private final int colSpan;
    private final int rowSpan;
    private String content;
}
