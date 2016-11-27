package org.xbib.jdbc.csv;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * This class implements the java.sql.ResultSetMetaData JDBC interface for the
 * CsvJdbc driver.
 */
public class CsvResultSetMetaData implements ResultSetMetaData {

    /**
     * Names of columns
     */
    private String[] columnNames;
    private String[] columnLabels;
    private String[] columnTypes;
    private int[] columnDisplaySizes;
    /**
     * Name of table
     */
    private String tableName;

    /**
     * Constructor for the CsvResultSetMetaData object
     *
     * @param tableName   Name of table
     * @param columnTypes Names of columns in table
     */
    CsvResultSetMetaData(String tableName, String[] columnNames,
                         String[] columnLabels, String[] columnTypes,
                         int[] columnDisplaySizes) {
        this.tableName = tableName;
        this.columnNames = columnNames;
        this.columnLabels = columnLabels;
        this.columnTypes = columnTypes;
        this.columnDisplaySizes = columnDisplaySizes;
    }

    @Override
    public String getColumnClassName(int column) throws SQLException {
        return columnTypes[column - 1];
    }

    @Override
    public int getColumnCount() throws SQLException {
        return columnTypes.length;
    }

    @Override
    public String getCatalogName(int column) throws SQLException {
        return "";
    }

    @Override
    public int getColumnDisplaySize(int column) throws SQLException {
        return columnDisplaySizes[column - 1];
    }

    @Override
    public boolean isAutoIncrement(int column) throws SQLException {
        return false;
    }

    @Override
    public boolean isCaseSensitive(int column) throws SQLException {
        // all columns are uppercase
        return false;
    }

    @Override
    public boolean isSearchable(int column) throws SQLException {
        // the implementation doesn't support the where clause
        return false;
    }

    @Override
    public boolean isCurrency(int column) throws SQLException {
        return false;
    }

    @Override
    public int isNullable(int column) throws SQLException {
        return ResultSetMetaData.columnNullableUnknown;
    }

    @Override
    public boolean isSigned(int column) throws SQLException {
        return false;
    }

    @Override
    public String getColumnLabel(int column) throws SQLException {
        // SQL column numbers start at 1
        return columnLabels[column - 1];
    }

    @Override
    public String getColumnName(int column) throws SQLException {
        // SQL column numbers start at 1
        return columnNames[column - 1];
    }

    @Override
    public String getSchemaName(int column) throws SQLException {
        return "";
    }

    @Override
    public int getPrecision(int column) throws SQLException {
        // All the fields are text, should this throw an SQLException?
        return 0;
    }

    @Override
    public int getScale(int column) throws SQLException {
        // All the fields are text, should this throw an SQLException?
        return 0;
    }

    @Override
    public String getTableName(int column) throws SQLException {
        return tableName;
    }

    private Map<String, Integer> typeNameToTypeCode = new HashMap<String, Integer>() {
        private static final long serialVersionUID = -8819579540085202365L;

        {
            put("String", Types.VARCHAR);
            put("Boolean", Types.BOOLEAN);
            put("Byte", Types.TINYINT);
            put("Short", Types.SMALLINT);
            put("Int", Types.INTEGER);
            put("Integer", Types.INTEGER);
            put("Long", Types.BIGINT);
            put("Float", Types.FLOAT);
            put("Double", Types.DOUBLE);
            put("BigDecimal", Types.DECIMAL);
            put("Date", Types.DATE);
            put("Time", Types.TIME);
            put("Timestamp", Types.TIMESTAMP);
            put("Blob", Types.BLOB);
            put("Clob", Types.CLOB);
            put("expression", Types.BLOB);
        }
    };

    @Override
    public int getColumnType(int column) throws SQLException {
        String columnTypeName = getColumnTypeName(column);
        return typeNameToTypeCode.get(columnTypeName);
    }

    @Override
    public String getColumnTypeName(int column) throws SQLException {
        return columnTypes[column - 1];
    }

    @Override
    public boolean isReadOnly(int column) throws SQLException {
        return true;
    }

    @Override
    public boolean isWritable(int column) throws SQLException {
        return false;
    }

    @Override
    public boolean isDefinitelyWritable(int column) throws SQLException {
        return false;
    }

    @Override
    public boolean isWrapperFor(Class<?> arg0) throws SQLException {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> arg0) throws SQLException {
        return null;
    }
}
