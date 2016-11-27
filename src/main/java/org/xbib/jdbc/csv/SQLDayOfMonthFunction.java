package org.xbib.jdbc.csv;

import java.util.Calendar;

/**
 *
 */
class SQLDayOfMonthFunction extends SQLCalendarFunction {
    public SQLDayOfMonthFunction(Expression expression) {
        super("DAYOFMONTH", Calendar.DAY_OF_MONTH, expression);
    }
}
