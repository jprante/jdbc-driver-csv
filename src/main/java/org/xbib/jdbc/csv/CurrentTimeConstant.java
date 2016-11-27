package org.xbib.jdbc.csv;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 */
class CurrentTimeConstant extends Expression {
    ExpressionParser parent;

    public CurrentTimeConstant(ExpressionParser parent) {
        this.parent = parent;
    }

    public Object eval(Map<String, Object> env) {
        return parent.getCurrentTime();
    }

    public String toString() {
        return "CURRENT_TIME";
    }

    public List<String> usedColumns() {
        return new LinkedList<String>();
    }
}
