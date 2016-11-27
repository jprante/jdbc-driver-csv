package org.xbib.jdbc.csv;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 */
class NumericConstant extends Expression {
    Number value;

    public NumericConstant(Number d) {
        value = d;
    }

    public Object eval(Map<String, Object> env) {
        return value;
    }

    public String toString() {
        return value.toString();
    }

    public List<String> usedColumns() {
        return new LinkedList<String>();
    }
}
