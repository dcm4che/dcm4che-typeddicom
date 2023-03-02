package org.dcm4che.typeddicom.parser.metamodel;


import java.io.Serializable;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MacroMetaInfo extends DataElementMetaInfoContainer implements Serializable {
    private final String tableId;

    public MacroMetaInfo(String tableId) {
        this.tableId = tableId;
    }

    public String getTableId() {
        return tableId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MacroMetaInfo that = (MacroMetaInfo) o;
        return Objects.equals(tableId, that.tableId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tableId);
    }

    public String implementsBuilderInterfaces() {
        return StreamSupport.stream(getSubDataElementMetaInfos().spliterator(), false)
                .map(dataElementMetaInfo -> dataElementMetaInfo.getKeyword() + ".Builder<SELF>")
                .collect(Collectors.joining(", "));
    }
}
