package org.xbib.jdbc.csv;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 */
class SQLMinFunction extends AggregateFunction {
    boolean isDistinct;
    Expression expression;
    Object min = null;

    public SQLMinFunction(boolean isDistinct, Expression expression) {
        this.isDistinct = isDistinct;
        this.expression = expression;
    }

    public Object eval(Map<String, Object> env) throws SQLException {
        Object o = env.get(GROUPING_COLUMN_NAME);
        if (o != null) {
            /*
			 * Find the minimum from the rows grouped together
			 * by the GROUP BY clause.
			 */
            List groupRows = (List) o;
            Object minInGroup = null;
            for (int i = 0; i < groupRows.size(); i++) {
                o = expression.eval((Map) groupRows.get(i));
                if (o != null && (minInGroup == null || ((Comparable) minInGroup).compareTo(o) > 0)) {
                    minInGroup = o;
                }
            }
            return minInGroup;
        }
        return min;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("MIN(");
        if (isDistinct) {
            sb.append("DISTINCT ");
        }
        sb.append(expression);
        sb.append(")");
        return sb.toString();
    }

    public List<String> usedColumns() {
        return new LinkedList<String>();
    }

    public List<String> aggregateColumns() {
        List<String> result = new LinkedList<String>();
        result.addAll(expression.usedColumns());
        return result;
    }

    public List<AggregateFunction> aggregateFunctions() {
        List<AggregateFunction> result = new LinkedList<AggregateFunction>();
        result.add(this);
        return result;
    }

    public void processRow(Map<String, Object> env) throws SQLException {
		/*
		 * Only consider non-null values.
		 */
        Object o = expression.eval(env);
        if (o != null && (min == null || ((Comparable) min).compareTo(o) > 0)) {
            min = o;
        }
    }
}
