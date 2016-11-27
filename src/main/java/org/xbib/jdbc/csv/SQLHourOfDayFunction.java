package org.xbib.jdbc.csv;

import java.util.Calendar;

/**
 *
 */
class SQLHourOfDayFunction extends SQLCalendarFunction {
    public SQLHourOfDayFunction(Expression expression) {
        super("HOUROFDAY", Calendar.HOUR_OF_DAY, expression);
    }
}
