package org.xbib.jdbc.csv;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 */
class SQLCoalesceFunction extends Expression {
    List<Expression> expressions;

    public SQLCoalesceFunction(List<Expression> expressions) {
        this.expressions = expressions;
    }

    public Object eval(Map<String, Object> env) throws SQLException {
        Object retval = null;

		/*
         * Find first expression that does not evaluate to NULL.
		 */
        Iterator<Expression> it = expressions.iterator();
        while (retval == null && it.hasNext()) {
            Expression expr = it.next();
            retval = expr.eval(env);
        }
        return retval;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("COALESCE(");
        String delimiter = "";
        Iterator<Expression> it = expressions.iterator();
        while (it.hasNext()) {
            sb.append(delimiter);
            sb.append(it.next().toString());
            delimiter = ",";
        }
        sb.append(")");
        return sb.toString();
    }

    public List<String> usedColumns() {
        List<String> result = new LinkedList<String>();
        Iterator<Expression> it = expressions.iterator();
        while (it.hasNext()) {
            result.addAll(it.next().usedColumns());
        }
        return result;
    }

    public List<AggregateFunction> aggregateFunctions() {
        List<AggregateFunction> result = new LinkedList<AggregateFunction>();
        Iterator<Expression> it = expressions.iterator();
        while (it.hasNext()) {
            result.addAll(it.next().aggregateFunctions());
        }
        return result;
    }
}
