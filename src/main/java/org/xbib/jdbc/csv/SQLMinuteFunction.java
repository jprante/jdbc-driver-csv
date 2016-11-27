package org.xbib.jdbc.csv;

import java.util.Calendar;

/**
 *
 */
class SQLMinuteFunction extends SQLCalendarFunction {
    public SQLMinuteFunction(Expression expression) {
        super("MINUTE", Calendar.MINUTE, expression);
    }
}
