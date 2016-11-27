package org.xbib.jdbc.csv;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 */
class OrExpression extends LogicalExpression {
    LogicalExpression left, right;
    boolean isValid;

    public OrExpression(Expression left, Expression right) {
        isValid = (left instanceof LogicalExpression && right instanceof LogicalExpression);
        if (isValid) {
            this.left = (LogicalExpression) left;
            this.right = (LogicalExpression) right;
            isValid = left.isValid() && right.isValid();
        }
    }

    public boolean isValid() {
        return isValid;
    }

    public boolean isTrue(Map<String, Object> env) throws SQLException {
        return left.isTrue(env) || right.isTrue(env);
    }

    public String toString() {
        return "OR " + left + " " + right;
    }

    public List<String> usedColumns() {
        List<String> result = new LinkedList<String>();
        result.addAll(left.usedColumns());
        result.addAll(right.usedColumns());
        return result;
    }

    public List<AggregateFunction> aggregateFunctions() {
        List<AggregateFunction> result = new LinkedList<AggregateFunction>();
        result.addAll(left.aggregateFunctions());
        result.addAll(right.aggregateFunctions());
        return result;
    }
}
