package org.xbib.jdbc.csv;

import java.util.LinkedList;
import java.util.List;

/**
 *
 */
class AsteriskExpression extends Expression {
    String expression;

    public AsteriskExpression(String expression) {
        this.expression = expression;
    }

    public String toString() {
        return expression;
    }

    public List<String> usedColumns() {
        List<String> result = new LinkedList<String>();
        return result;
    }
}
