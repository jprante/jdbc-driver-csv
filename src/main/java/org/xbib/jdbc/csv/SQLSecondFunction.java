package org.xbib.jdbc.csv;

import java.util.Calendar;

/**
 *
 */
class SQLSecondFunction extends SQLCalendarFunction {
    public SQLSecondFunction(Expression expression) {
        super("SECOND", Calendar.SECOND, expression);
    }
}
