package org.xbib.jdbc.csv;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 *
 */
class NotExpression extends LogicalExpression {
    LogicalExpression content;
    boolean isValid;

    public NotExpression(Expression arg) {
        isValid = (arg instanceof LogicalExpression);
        if (isValid) {
            this.content = (LogicalExpression) arg;
            isValid = this.content.isValid();
        }
    }

    public boolean isValid() {
        return isValid;
    }

    public boolean isTrue(Map<String, Object> env) throws SQLException {
        return !content.isTrue(env);
    }

    public String toString() {
        return "NOT " + content;
    }

    public List<String> usedColumns() {
        return content.usedColumns();
    }

    public List<AggregateFunction> aggregateFunctions() {
        return content.aggregateFunctions();
    }
}
