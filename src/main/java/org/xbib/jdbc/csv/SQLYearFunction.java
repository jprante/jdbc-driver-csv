package org.xbib.jdbc.csv;

import java.util.Calendar;

/**
 *
 */
class SQLYearFunction extends SQLCalendarFunction {
    public SQLYearFunction(Expression expression) {
        super("YEAR", Calendar.YEAR, expression);
    }
}
