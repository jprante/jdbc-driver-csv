package org.xbib.jdbc.csv;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 */
class SQLCalendarFunction extends Expression {
    String functionName;
    int calendarField;
    Expression expression;

    public SQLCalendarFunction(String functionName, int calendarField,
                               Expression expression) {
        this.functionName = functionName;
        this.calendarField = calendarField;
        this.expression = expression;
    }

    public Object eval(Map<String, Object> env) throws SQLException {
        Object retval = null;
        Object o = expression.eval(env);
        if (o != null) {
            /*
			 * Accept either java.sql.Date, java.sql.Time or java.sql.Timestamp.
			 */
            java.util.Date date = null;
            if (o instanceof java.util.Date) {
                date = (java.util.Date) o;
            } else {
				/*
				 * Try and convert from String to a Timestamp or Date/Time.
				 */
                Expression stringConverter = new ColumnName(StringConverter.COLUMN_NAME);
                StringConverter sc = (StringConverter) stringConverter.eval(env);
                date = sc.parseTimestamp(o.toString());
                if (date == null) {
                    if (calendarField == Calendar.DAY_OF_MONTH ||
                            calendarField == Calendar.MONTH ||
                            calendarField == Calendar.YEAR) {
                        date = sc.parseDate(o.toString());
                    } else {
                        date = sc.parseTime(o.toString());
                    }
                }
            }
            if (date != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int fieldValue = cal.get(calendarField);
                if (calendarField == Calendar.MONTH) {
                    fieldValue++;
                }
                retval = fieldValue;
            }
        }
        return retval;
    }

    public String toString() {
        return functionName + "(" + expression + ")";
    }

    public List<String> usedColumns() {
        List<String> result = new LinkedList<String>();
        result.addAll(expression.usedColumns());
        return result;
    }

    public List<AggregateFunction> aggregateFunctions() {
        List<AggregateFunction> result = new LinkedList<AggregateFunction>();
        result.addAll(expression.aggregateFunctions());
        return result;
    }
}
