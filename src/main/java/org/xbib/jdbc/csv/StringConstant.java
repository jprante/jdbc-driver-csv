package org.xbib.jdbc.csv;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 */
class StringConstant extends Expression {
    String value;

    public StringConstant(String s) {
        value = s;
    }

    public Object eval(Map<String, Object> env) {
        return value;
    }

    public String toString() {
        return "'" + value + "'";
    }

    public List<String> usedColumns() {
        return new LinkedList<String>();
    }
}
