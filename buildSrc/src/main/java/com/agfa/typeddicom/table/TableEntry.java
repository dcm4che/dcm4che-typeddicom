package com.agfa.typeddicom.table;

import java.io.Serializable;
import java.util.List;

/**
 * TODO describe this class
 *
 * @author (. @ agfa.com)
 */
public interface TableEntry extends Serializable {
    List<TableEntry> getSubTableEntries();
}
