package org.xbib.jdbc.csv;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 */
class SQLCountFunction extends AggregateFunction {
    HashSet<Object> distinctValues;
    Expression expression;
    int counter = 0;

    public SQLCountFunction(boolean isDistinct, Expression expression) {
        if (isDistinct) {
            this.distinctValues = new HashSet<Object>();
        }
        this.expression = expression;
    }

    public Object eval(Map<String, Object> env) throws SQLException {
        Integer retval;
        Object o = env.get(GROUPING_COLUMN_NAME);
        if (o != null) {
            /*
			 * The count is the number of rows grouped together
			 * by the GROUP BY clause.
			 */
            List groupRows = (List) o;
            if (this.distinctValues != null) {
                HashSet<Object> unique = new HashSet<Object>();
                for (int i = 0; i < groupRows.size(); i++) {
                    o = expression.eval((Map) groupRows.get(i));
                    if (o != null) {
                        unique.add(o);
                    }
                }
                retval = unique.size();
            } else {
                retval = groupRows.size();
            }
        } else {
            if (this.distinctValues != null) {
                retval = this.distinctValues.size();
            } else {
                retval = counter;
            }
        }
        return retval;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("COUNT(");
        if (distinctValues != null) {
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
        if (!(expression instanceof AsteriskExpression)) {
            result.addAll(expression.usedColumns());
        }
        return result;
    }

    public List<AggregateFunction> aggregateFunctions() {
        List<AggregateFunction> result = new LinkedList<AggregateFunction>();
        result.add(this);
        return result;
    }

    public void processRow(Map<String, Object> env) throws SQLException {
        if (expression instanceof AsteriskExpression) {
            counter++;
        } else {
			/*
			 * Only count non-null values.
			 */
            Object o = expression.eval(env);
            if (o != null) {
                counter++;
                if (distinctValues != null) {
					/*
					 * We want a count of DISTINCT values, so we have
					 * to keep a list of unique values.
					 */
                    distinctValues.add(o);
                }
            }
        }
    }
}
