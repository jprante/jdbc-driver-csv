package org.xbib.jdbc.csv;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 */
class SQLTrimFunction extends Expression {
    public enum Type {LEADING, TRAILING, BOTH}

    Expression expression;
    Expression trimChars;
    Type trimType;

    public SQLTrimFunction(Expression expression, Expression trimChars, Type trimType) {
        this.expression = expression;
        this.trimChars = trimChars;
        this.trimType = trimType;
    }

    public Object eval(Map<String, Object> env) throws SQLException {
        Object retval = expression.eval(env);
        if (retval != null) {
            String str = retval.toString();
            if (trimChars != null) {
                Object o = trimChars.eval(env);
                if (o != null) {
                    String trim = o.toString();
                    int startIndex = 0;
                    if (trimType == Type.LEADING || trimType == Type.BOTH) {
                        while (startIndex < str.length() && trim.indexOf(str.charAt(startIndex)) >= 0) {
                            startIndex++;
                        }
                    }

                    int endIndex = str.length() - 1;
                    if (trimType == Type.TRAILING || trimType == Type.BOTH) {
                        while (endIndex >= startIndex && trim.indexOf(str.charAt(endIndex)) >= 0) {
                            endIndex--;
                        }
                    }

                    if (endIndex >= startIndex) {
                        retval = str.substring(startIndex, endIndex + 1);
                    } else {
                        retval = "";
                    }
                } else {
                    retval = null;
                }
            } else {
                /*
				 * Trim whitespace by default.
				 */
                if (trimType == Type.BOTH) {
                    retval = str.trim();
                } else if (trimType == Type.LEADING) {
                    int startIndex = 0;
                    while (startIndex < str.length() && Character.isWhitespace(str.charAt(startIndex))) {
                        startIndex++;
                    }
                    retval = str.substring(startIndex, str.length());
                } else // Type.TRAILING
                {
                    int endIndex = str.length() - 1;
                    while (endIndex >= 0 && Character.isWhitespace(str.charAt(endIndex))) {
                        endIndex--;
                    }
                    retval = str.substring(0, endIndex + 1);
                }
            }
        }
        return retval;
    }

    public String toString() {
        if (trimType == Type.LEADING) {
            return "LTRIM(" + expression + ")";
        } else if (trimType == Type.TRAILING) {
            return "RTRIM(" + expression + ")";
        } else {
            return "TRIM(" + expression + ")";
        }
    }

    public List<String> usedColumns() {
        List<String> result = new LinkedList<String>();
        result.addAll(expression.usedColumns());
        if (trimChars != null) {
            result.addAll(trimChars.usedColumns());
        }
        return result;
    }

    public List<AggregateFunction> aggregateFunctions() {
        List<AggregateFunction> result = new LinkedList<AggregateFunction>();
        result.addAll(expression.aggregateFunctions());
        if (trimChars != null) {
            result.addAll(trimChars.aggregateFunctions());
        }
        return result;
    }
}
