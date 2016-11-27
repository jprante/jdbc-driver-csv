package org.xbib.jdbc.csv;

import java.sql.SQLException;
import java.util.Map;

/**
 *
 */
class QueryEnvEntry extends Expression {
    String key;
    Expression expression;

    public QueryEnvEntry(String fieldName, Expression exp) {
        this.key = fieldName;
        this.expression = exp;
    }

    public Object eval(Map<String, Object> env) throws SQLException {
        return expression.eval(env);
    }

    public String toString() {
        return key + ": " + expression.toString();
    }
}
