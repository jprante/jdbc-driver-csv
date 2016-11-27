package org.xbib.jdbc.csv;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 */
class InExpression extends LogicalExpression {
    Expression obj;
    List<Expression> inList;

    public InExpression(Expression obj, List<Expression> inList) {
        this.obj = obj;
        this.inList = inList;
    }

    public boolean isTrue(Map<String, Object> env) throws SQLException {
        Comparable objValue = (Comparable) obj.eval(env);
        for (Expression expr : inList) {
            Comparable exprValue = (Comparable) expr.eval(env);
            Integer compared = RelopExpression.compare(objValue, exprValue, env);
            if (compared != null && compared.intValue() == 0) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("IN ");
        sb.append(obj.toString());
        sb.append(" (");
        String delimiter = "";
        for (Expression expr : inList) {
            sb.append(delimiter);
            sb.append(expr.toString());
            delimiter = ", ";
        }
        sb.append(")");
        return sb.toString();
    }

    public List<String> usedColumns() {
        List<String> result = new LinkedList<String>();
        result.addAll(obj.usedColumns());
        for (Expression expr : inList) {
            result.addAll(expr.usedColumns());
        }
        return result;
    }

    public List<AggregateFunction> aggregateFunctions() {
        List<AggregateFunction> result = new LinkedList<AggregateFunction>();
        for (Expression expr : inList) {
            result.addAll(expr.aggregateFunctions());
        }
        return result;
    }
}
