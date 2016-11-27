package org.xbib.jdbc.csv;

import java.sql.SQLException;
import java.util.Map;

/**
 *
 */
abstract class LogicalExpression extends Expression {
    public boolean isTrue(Map<String, Object> env) throws SQLException {
        return false;
    }
}
