package org.xbib.jdbc.io;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A reader from a list, enabling database metadata functions to return JDBC ResultSet objects
 * containing lists of tables, schemas and other metadata.
 */
public class ListDataReader extends DataReader {
    private String[] columnNames;
    private String[] columnTypes;
    private List<Object[]> columnValues;
    private int rowIndex;

    public ListDataReader(String[] columnNames, String[] columnTypes, List<Object[]> columnValues) {
        this.columnNames = columnNames;
        this.columnTypes = columnTypes;
        this.columnValues = columnValues;
        rowIndex = -1;
    }

    @Override
    public boolean next() throws SQLException {
        rowIndex++;
        return (rowIndex < columnValues.size());
    }

    @Override
    public String[] getColumnNames() throws SQLException {
        return columnNames;
    }

    @Override
    public Object getField(int i) throws SQLException {
        Object[] o = columnValues.get(rowIndex);
        return o[i];
    }

    @Override
    public void close() throws SQLException {
    }

    @Override
    public Map<String, Object> getEnvironment() throws SQLException {
        HashMap<String, Object> retval = new HashMap<String, Object>();
        Object[] o = columnValues.get(rowIndex);
        for (int i = 0; i < columnNames.length; i++) {
            retval.put(columnNames[i], o[i]);
        }
        return retval;
    }

    @Override
    public String[] getColumnTypes() throws SQLException {
        return columnTypes;
    }

    @Override
    public int[] getColumnSizes() throws SQLException {
        int[] columnSizes = new int[columnTypes.length];
        Arrays.fill(columnSizes, DEFAULT_COLUMN_SIZE);
        return columnSizes;
    }

    @Override
    public String getTableAlias() {
        return null;
    }
}
