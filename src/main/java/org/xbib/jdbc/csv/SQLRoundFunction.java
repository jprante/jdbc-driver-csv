package org.xbib.jdbc.csv;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 */
class SQLRoundFunction extends Expression {
    Expression expression;

    public SQLRoundFunction(Expression expression) {
        this.expression = expression;
    }

    public Object eval(Map<String, Object> env) throws SQLException {
        Object retval = expression.eval(env);
        if (retval != null) {
            if (!(retval instanceof Number)) {
                try {
                    retval = new Double(retval.toString());
                } catch (NumberFormatException e) {
                    retval = null;
                }
            }
            if (retval != null) {
                if (retval instanceof Short) {
                    retval = ((Short) retval).intValue();
                } else if (!(retval instanceof Integer || retval instanceof Long)) {
                    double d = ((Number) retval).doubleValue();
                    if (d < Integer.MIN_VALUE || d > Integer.MAX_VALUE) {
                        retval = (double) Math.round(d);
                    } else {
                        retval = (int) Math.round(d);
                    }
                }
            }
        }
        return retval;
    }

    public String toString() {
        return "ROUND(" + expression + ")";
    }

    public List<String> usedColumns() {
        List<String> result = new LinkedList<String>();
        result.addAll(expression.usedColumns());
        return result;
    }

    public List<AggregateFunction> aggregateFunctions() {
        List<AggregateFunction> result = new LinkedList<AggregateFunction>();
        result.addAll(expression.aggregateFunctions());
        return result;
    }
}
