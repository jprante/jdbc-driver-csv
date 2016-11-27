package org.xbib.jdbc.csv;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 */
class SQLNullIfFunction extends Expression {
    Expression expression1;
    Expression expression2;

    public SQLNullIfFunction(Expression expression1, Expression expression2) {
        this.expression1 = expression1;
        this.expression2 = expression2;
    }

    public Object eval(Map<String, Object> env) throws SQLException {
        Object retval;
        Comparable value1 = (Comparable) expression1.eval(env);
        Comparable value2 = (Comparable) expression2.eval(env);
        Integer compared = RelopExpression.compare(value1, value2, env);

        if (compared != null && compared == 0) {
            retval = null;
        } else {
            retval = value1;
        }
        return retval;
    }

    public String toString() {
        return "NULLIF(" + expression1 + "," + expression2 + ")";
    }

    public List<String> usedColumns() {
        List<String> result = new LinkedList<String>();
        result.addAll(expression1.usedColumns());
        result.addAll(expression2.usedColumns());
        return result;
    }

    public List<AggregateFunction> aggregateFunctions() {
        List<AggregateFunction> result = new LinkedList<AggregateFunction>();
        result.addAll(expression1.aggregateFunctions());
        result.addAll(expression2.aggregateFunctions());
        return result;
    }
}
