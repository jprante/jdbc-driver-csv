package org.xbib.jdbc.csv;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class LikeExpression extends LogicalExpression {
    Expression arg1, arg2;
    Expression escapeArg;

    public LikeExpression(Expression arg1, Expression arg2, Expression escapeArg) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.escapeArg = escapeArg;
    }

    public boolean isTrue(Map<String, Object> env) throws SQLException {
        Object left = arg1.eval(env);
        Object right = arg2.eval(env);
        String escape = LikePattern.DEFAULT_ESCAPE_STRING;

        if (escapeArg != null) {
            escape = "";
            Object o = escapeArg.eval(env);
            if (o != null) {
                String s = o.toString();
                if (s.length() > 0) {
                    escape = s.substring(0, 1);
                }
            }
        }

        boolean result = false;
        if (left != null && right != null) {
            result = LikePattern.matches(right.toString(), escape, left.toString());
        }
        return result;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("L ");
        sb.append(arg1);
        sb.append(" ");
        sb.append(arg2);
        if (escapeArg != null) {
            sb.append(" ESCAPE ");
            sb.append(escapeArg);
        }
        return sb.toString();
    }

    public List<String> usedColumns() {
        List<String> result = new LinkedList<String>();
        result.addAll(arg1.usedColumns());
        result.addAll(arg2.usedColumns());
        if (escapeArg != null) {
            result.addAll(escapeArg.usedColumns());
        }
        return result;
    }

    public List<AggregateFunction> aggregateFunctions() {
        List<AggregateFunction> result = new LinkedList<AggregateFunction>();
        result.addAll(arg1.aggregateFunctions());
        result.addAll(arg2.aggregateFunctions());
        if (escapeArg != null) {
            result.addAll(escapeArg.aggregateFunctions());
        }
        return result;
    }
}
