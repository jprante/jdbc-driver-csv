package org.xbib.jdbc.csv;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 */
class CurrentDateConstant extends Expression {
    ExpressionParser parent;

    public CurrentDateConstant(ExpressionParser parent) {
        this.parent = parent;
    }

    public Object eval(Map<String, Object> env) {
        return parent.getCurrentDate();
    }

    public String toString() {
        return "CURRENT_DATE";
    }

    public List<String> usedColumns() {
        return new LinkedList<String>();
    }
}
