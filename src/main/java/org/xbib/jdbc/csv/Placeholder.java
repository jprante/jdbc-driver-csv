package org.xbib.jdbc.csv;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 */
class Placeholder extends Expression {
    static int nextIndex = 1;
    private int index;

    public Placeholder() {
        index = nextIndex;
        nextIndex++;
    }

    public Object eval(Map<String, Object> env) {
        return env.get("?" + index);
    }

    public String toString() {
        return "?";
    }

    public List<String> usedColumns() {
        return new LinkedList<>();
    }
}
