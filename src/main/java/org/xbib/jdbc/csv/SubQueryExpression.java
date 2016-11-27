package org.xbib.jdbc.csv;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 */
class SubQueryExpression extends Expression {
    private ParsedStatement parsedStatement;

    public SubQueryExpression(ParsedStatement parsedStatement) {
        this.parsedStatement = parsedStatement;
    }

    public Object eval(Map<String, Object> env) throws SQLException {
        throw new SQLException(CsvResources.getString("subqueryNotSupported"));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(parsedStatement.toString());
        sb.append(")");
        return sb.toString();
    }

    public List<String> usedColumns() {
        return new LinkedList<String>();
    }

    public List<AggregateFunction> aggregateFunctions() {
        return new LinkedList<AggregateFunction>();
    }
}
