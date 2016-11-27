package org.xbib.jdbc.csv;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 */
class SearchedCaseExpression extends Expression {
    private List<Expression> conditions;
    private List<Expression> values;
    private Expression elseExpression;

    public SearchedCaseExpression(List<Expression> conditions,
                                  List<Expression> values, Expression elseExpression) {
        this.conditions = conditions;
        this.values = values;
        this.elseExpression = elseExpression;
    }

    public Object eval(Map<String, Object> env) throws SQLException {
        for (int i = 0; i < conditions.size(); i++) {
            Expression condition = conditions.get(i);
            if (!(condition instanceof LogicalExpression && condition.isValid())) {
                throw new SQLException(CsvResources.getString("caseNotLogical"));
            }
            if (((LogicalExpression) condition).isTrue(env)) {
                return values.get(i).eval(env);
            }
        }
        if (elseExpression != null) {
            return elseExpression.eval(env);
        }

        return null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("CASE");
        for (int i = 0; i < conditions.size(); i++) {
            Expression condition = conditions.get(i);
            sb.append(" WHEN ").append(condition.toString()).append(" THEN ").append(values.get(i));
        }
        if (elseExpression != null) {
            sb.append(" ELSE ").append(elseExpression.toString());
        }
        sb.append(" END");
        return sb.toString();
    }

    public List<String> usedColumns() {
        List<String> result = new LinkedList<String>();
        Iterator<Expression> it = conditions.iterator();
        while (it.hasNext()) {
            result.addAll(it.next().usedColumns());
        }
        it = values.iterator();
        while (it.hasNext()) {
            result.addAll(it.next().usedColumns());
        }
        if (elseExpression != null) {
            result.addAll(elseExpression.usedColumns());
        }
        return result;
    }

    public List<AggregateFunction> aggregateFunctions() {
        List<AggregateFunction> result = new LinkedList<AggregateFunction>();
        Iterator<Expression> it = conditions.iterator();
        while (it.hasNext()) {
            result.addAll(it.next().aggregateFunctions());
        }
        it = values.iterator();
        while (it.hasNext()) {
            result.addAll(it.next().aggregateFunctions());
        }
        if (elseExpression != null) {
            result.addAll(elseExpression.aggregateFunctions());
        }
        return result;
    }
}