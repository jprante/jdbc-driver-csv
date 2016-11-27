package org.xbib.jdbc.csv;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 */
class BetweenExpression extends LogicalExpression {
    Expression obj, left, right;

    public BetweenExpression(Expression obj, Expression left, Expression right) {
        this.obj = obj;
        this.left = left;
        this.right = right;
    }

    public boolean isTrue(Map<String, Object> env) throws SQLException {
        Comparable leftValue = (Comparable) left.eval(env);
        Comparable rightValue = (Comparable) right.eval(env);
        Comparable objValue = (Comparable) obj.eval(env);
        Integer comparedLeft = RelopExpression.compare(leftValue, objValue, env);
        boolean result = false;
        if (comparedLeft != null && comparedLeft <= 0) {
            Integer comparedRight = RelopExpression.compare(rightValue, objValue, env);
            if (comparedRight != null && comparedRight >= 0) {
                result = true;
            }
        }
        return result;
    }

    public String toString() {
        return "B " + obj + " " + left + " " + right;
    }

    public List<String> usedColumns() {
        List<String> result = new LinkedList<String>();
        result.addAll(obj.usedColumns());
        result.addAll(left.usedColumns());
        result.addAll(right.usedColumns());
        return result;
    }

    public List<AggregateFunction> aggregateFunctions() {
        List<AggregateFunction> result = new LinkedList<AggregateFunction>();
        result.addAll(obj.aggregateFunctions());
        result.addAll(left.aggregateFunctions());
        result.addAll(right.aggregateFunctions());
        return result;
    }
}
