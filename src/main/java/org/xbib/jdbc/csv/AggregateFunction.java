package org.xbib.jdbc.csv;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 *
 */
abstract class AggregateFunction extends Expression {
    /*
     * Key for column name in database rows that is accumulating
     * the aggregate function value.
     */
    public static final String GROUPING_COLUMN_NAME = "@GROUPROWS";

    public abstract List<String> aggregateColumns();

    public abstract void processRow(Map<String, Object> env) throws SQLException;
}
