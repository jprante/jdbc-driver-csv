package org.xbib.jdbc.csv;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 */
class IsNullExpression extends LogicalExpression {
    Expression arg;

    public IsNullExpression(Expression arg) {
        this.arg = arg;
    }

    public boolean isTrue(Map<String, Object> env) throws SQLException {
        Object o = arg.eval(env);
        return (o == null);
    }

    public String toString() {
        return "N " + arg;
    }

    public List<String> usedColumns() {
        List<String> result = new LinkedList<String>();
        result.addAll(arg.usedColumns());
        return result;
    }

    public List<AggregateFunction> aggregateFunctions() {
        List<AggregateFunction> result = new LinkedList<AggregateFunction>();
        result.addAll(arg.aggregateFunctions());
        return result;
    }
}
