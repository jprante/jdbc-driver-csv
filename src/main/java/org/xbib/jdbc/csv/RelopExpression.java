package org.xbib.jdbc.csv;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 */
class RelopExpression extends LogicalExpression {
    String op;
    Expression left, right;
    boolean isValid;

    public RelopExpression(String op, Expression left, Expression right) {
        isValid = !(left instanceof LogicalExpression || right instanceof LogicalExpression);
        this.op = op;
        this.left = left;
        this.right = right;
    }

    public boolean isTrue(Map<String, Object> env) throws SQLException {
        boolean result = false;
        Comparable leftValue = (Comparable) left.eval(env);
        Comparable rightValue = (Comparable) right.eval(env);
        Integer leftComparedToRightObj = compare(leftValue, rightValue, env);
        if (leftComparedToRightObj != null) {
            int leftComparedToRight = leftComparedToRightObj;
            if (leftValue != null && rightValue != null) {
                switch (op) {
                    case "=":
                        result = leftComparedToRight == 0;
                        break;
                    case "<>":
                    case "!=":
                        result = leftComparedToRight != 0;
                        break;
                    case ">":
                        result = leftComparedToRight > 0;
                        break;
                    case "<":
                        result = leftComparedToRight < 0;
                        break;
                    case "<=":
                    case "=<":
                        result = leftComparedToRight <= 0;
                        break;
                    case ">=":
                    case "=>":
                        result = leftComparedToRight >= 0;
                        break;
                }
            }
        }
        return result;
    }

    public static Integer compare(Comparable leftValue,
                                  Comparable rightValue, Map<String, Object> env) throws SQLException {
        Integer leftComparedToRightObj = null;
        try {
            if (leftValue != null && rightValue != null) {
                leftComparedToRightObj = leftValue.compareTo(rightValue);
            }
        } catch (ClassCastException e) {
        }
        try {
            if (leftComparedToRightObj == null) {
                if (leftValue == null || rightValue == null) {
                    /*
					 * Do nothing.  Anything compared with NULL is false.
					 */
                } else if (leftValue instanceof Date) {
                    Expression stringConverter = new ColumnName(StringConverter.COLUMN_NAME);
                    StringConverter sc = (StringConverter) stringConverter.eval(env);
                    Date date = sc.parseDate(rightValue.toString());
                    if (date != null) {
                        leftComparedToRightObj = leftValue.compareTo(date);
                    }
                } else if (rightValue instanceof Date) {
                    Expression stringConverter = new ColumnName(StringConverter.COLUMN_NAME);
                    StringConverter sc = (StringConverter) stringConverter.eval(env);
                    Date date = sc.parseDate(leftValue.toString());
                    if (date != null) {
                        leftComparedToRightObj = date.compareTo((Date) rightValue);
                    }
                } else if (leftValue instanceof Time) {
                    Expression stringConverter = new ColumnName(StringConverter.COLUMN_NAME);
                    StringConverter sc = (StringConverter) stringConverter.eval(env);
                    Time time = sc.parseTime(rightValue.toString());
                    if (time != null) {
                        leftComparedToRightObj = leftValue.compareTo(time);
                    }
                } else if (rightValue instanceof Time) {
                    Expression stringConverter = new ColumnName(StringConverter.COLUMN_NAME);
                    StringConverter sc = (StringConverter) stringConverter.eval(env);
                    Time time = sc.parseTime(leftValue.toString());
                    if (time != null) {
                        leftComparedToRightObj = time.compareTo((Time) rightValue);
                    }
                } else if (leftValue instanceof Timestamp) {
                    Expression stringConverter = new ColumnName(StringConverter.COLUMN_NAME);
                    StringConverter sc = (StringConverter) stringConverter.eval(env);
                    Timestamp timestamp = sc.parseTimestamp(rightValue.toString());
                    if (timestamp != null) {
                        leftComparedToRightObj = leftValue.compareTo(timestamp);
                    }
                } else if (rightValue instanceof Timestamp) {
                    Expression stringConverter = new ColumnName(StringConverter.COLUMN_NAME);
                    StringConverter sc = (StringConverter) stringConverter.eval(env);
                    Timestamp timestamp = sc.parseTimestamp(leftValue.toString());
                    if (timestamp != null) {
                        leftComparedToRightObj = timestamp.compareTo((Timestamp) rightValue);
                    }
                } else if (leftValue instanceof Boolean) {
                    Boolean leftBoolean = (Boolean) leftValue;
                    Boolean rightBoolean = Boolean.valueOf(rightValue.toString());

                    // false (0) is less than true (1) in Boolean algebra
                    if (leftBoolean.equals(rightBoolean)) {
                        leftComparedToRightObj = 0;
                    } else if (!leftBoolean) {
                        leftComparedToRightObj = -1;
                    } else {
                        leftComparedToRightObj = 1;
                    }
                } else {
                    Double leftDouble = new Double(leftValue.toString());
                    Double rightDouble = new Double(rightValue.toString());
                    leftComparedToRightObj = leftDouble.compareTo(rightDouble);
                }
            }
        } catch (ClassCastException | NumberFormatException e) {
            //
        }
        return leftComparedToRightObj;
    }

    public String toString() {
        return op + " " + left + " " + right;
    }

    public List<String> usedColumns() {
        List<String> result = new LinkedList<String>();
        result.addAll(left.usedColumns());
        result.addAll(right.usedColumns());
        return result;
    }

    public List<AggregateFunction> aggregateFunctions() {
        List<AggregateFunction> result = new LinkedList<AggregateFunction>();
        result.addAll(left.aggregateFunctions());
        result.addAll(right.aggregateFunctions());
        return result;
    }

    public boolean isValid() {
        return isValid;
    }
}
