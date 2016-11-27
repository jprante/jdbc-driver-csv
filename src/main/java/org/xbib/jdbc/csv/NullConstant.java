package org.xbib.jdbc.csv;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 */
class NullConstant extends Expression {
    public Object eval(Map<String, Object> env) {
        return null;
    }

    public String toString() {
        return "null";
    }

    public List<String> usedColumns() {
        return new LinkedList<String>();
    }
}
