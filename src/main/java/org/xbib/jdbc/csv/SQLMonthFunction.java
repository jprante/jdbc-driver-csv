package org.xbib.jdbc.csv;

import java.util.Calendar;

/**
 *
 */
class SQLMonthFunction extends SQLCalendarFunction {
    public SQLMonthFunction(Expression expression) {
        super("MONTH", Calendar.MONTH, expression);
    }
}
