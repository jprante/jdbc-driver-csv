package org.xbib.jdbc.csv;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 */
class ColumnName extends Expression {
    String columnName;

    public ColumnName(String columnName) {
        this.columnName = columnName.toUpperCase();
    }

    public Object eval(Map<String, Object> env) {
        return env.get(columnName);
    }

    public String toString() {
        return "[" + columnName + "]";
    }

    public List<String> usedColumns() {
        List<String> result = new LinkedList<String>();
        result.add(columnName);
        return result;
    }
}
