package com.agfa.typeddicom.table;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * TODO describe this class
 *
 * @author Niklas Roth (niklas.roth@agfa.com)
 */
@Data
public class Table {
    private final String id;
    private String caption;
    private final List<String> columnHeaders = new ArrayList<>();
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private List<List<TableCell>> cells = new ArrayList<>();

    public TableCell getTableCell(int row, int column) {
        if (row >= cells.size()) {
            return null;
        }
        List<TableCell> rowList = cells.get(row);
        if (column >= rowList.size()) {
            return null;
        }
        return rowList.get(column);
    }

    public TableCell getTableCell(int row, String columnHeader) {
        int colIndex = columnHeaders.indexOf(columnHeader);
        if (colIndex < 0) {
            return null;
        }
        return getTableCell(row, colIndex);
    }
    
    public void addTableCell(int row, TableCell tableCell) {
        int startColumn = 0;
        for (int r = row; r < row + tableCell.getRowSpan(); r++) {
            List<TableCell> tableCellsInRow;
            if (r < cells.size()) {
                tableCellsInRow = cells.get(r);
            } else {
                tableCellsInRow = new ArrayList<>(columnHeaders.size());
                cells.add(tableCellsInRow);
            }
            if (r == row) {
                startColumn = tableCellsInRow.size();
            }
            while (tableCellsInRow.size() < startColumn) {
                tableCellsInRow.add(null);
            }
            int columnsToAdd = tableCell.getColSpan();
            for (int i = startColumn; i < tableCellsInRow.size(); i++) {
                if (tableCellsInRow.get(i) == null) {
                    tableCellsInRow.set(i, tableCell);
                    columnsToAdd--;
                }
            }
            while (columnsToAdd > 0) {
                tableCellsInRow.add(tableCell);
                columnsToAdd--;
            }
        }
    }

    public int getRows() {
        return this.cells.size();
    }
    
    public int getColumns() {
        return this.cells.stream()
                .mapToInt(Collection::size)
                .max().orElse(0);
    }
}
