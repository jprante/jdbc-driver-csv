package org.xbib.jdbc.csv;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
class ParsedExpression extends LogicalExpression {
    public Expression content;
    private Map<String, Object> placeholders;

    public ParsedExpression(Expression left) {
        content = left;
        placeholders = new HashMap<String, Object>();
    }

    public boolean isValid() {
        return content.isValid();
    }

    public boolean isTrue(Map<String, Object> env) throws SQLException {
        if (!placeholders.isEmpty()) {
            /*
			 * Add prepared statement placeholders to environment.
			 */
            Map<String, Object> useThisEnv = new HashMap<String, Object>();
            useThisEnv.putAll(env);
            useThisEnv.putAll(placeholders);
            env = useThisEnv;
        }
        return ((LogicalExpression) content).isTrue(env);
    }

    public Object eval(Map<String, Object> env) throws SQLException {
        if (!placeholders.isEmpty()) {
			/*
			 * Add prepared statement placeholders to environment.
			 */
            Map<String, Object> useThisEnv = new HashMap<String, Object>();
            useThisEnv.putAll(env);
            useThisEnv.putAll(placeholders);
            env = useThisEnv;
        }
        return content.eval(env);
    }

    public String toString() {
        return content.toString();
    }

    public List<String> usedColumns() {
        return content.usedColumns();
    }

    public List<AggregateFunction> aggregateFunctions() {
        return content.aggregateFunctions();
    }

    public int getPlaceholdersCount() {
        return Placeholder.nextIndex - 1;
    }

    public void setPlaceholdersValues(Object[] values) {
        for (int i = 1; i < values.length; i++) {
            placeholders.put("?" + i, values[i]);
        }
    }
}
