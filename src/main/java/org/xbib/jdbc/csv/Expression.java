package org.xbib.jdbc.csv;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 */
public abstract class Expression {
    public Object eval(Map<String, Object> env) throws SQLException {
        return null;
    }

    public List<String> usedColumns() {
        return null;
    }

    public List<AggregateFunction> aggregateFunctions() {
        return new LinkedList<AggregateFunction>();
    }

    /**
     * Is this a valid expression such as A &gt; 5 or an invalid
     * mix of logical and arithmetic such as (A &gt; 5) + 1 that we
     * cannot detect during parsing.
     *
     * @return true if valid.
     */
    public boolean isValid() {
        return true;
    }
}
