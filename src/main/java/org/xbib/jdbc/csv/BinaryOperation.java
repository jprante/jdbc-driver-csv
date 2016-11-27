package org.xbib.jdbc.csv;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
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
class BinaryOperation extends Expression {
    private static final long MILLISECONDS_PER_DAY = 24 * 60 * 60 * 1000;
    String operation;
    char op;
    Expression left, right;

    public BinaryOperation(String operation, Expression left, Expression right) {
        this.operation = operation;
        this.op = operation.charAt(0);
        this.left = left;
        this.right = right;
    }

    public Object eval(Map<String, Object> env) throws SQLException {
        Object leftEval = left.eval(env);
        Object rightEval = right.eval(env);

        if (leftEval == null || rightEval == null) {
            return null;
        }

        try {
            Integer leftInt;
            BigInteger bil;
            boolean isLongExpression = false;

            if (leftEval instanceof Short) {
                leftInt = ((Short) leftEval).intValue();
                bil = new BigInteger(leftInt.toString());
            } else if (leftEval instanceof Long) {
                bil = new BigInteger(leftEval.toString());
                isLongExpression = true;
            } else {
                leftInt = (Integer) leftEval;
                bil = new BigInteger(leftInt.toString());
            }
            Integer rightInt;
            BigInteger bir;
            if (rightEval instanceof Short) {
                rightInt = ((Short) rightEval).intValue();
                bir = new BigInteger(rightInt.toString());
            } else if (rightEval instanceof Long) {
                bir = new BigInteger(rightEval.toString());
                isLongExpression = true;
            } else {
                rightInt = (Integer) rightEval;
                bir = new BigInteger(rightInt.toString());
            }
            if (op == '+') {
                bil = bil.add(bir);
            } else if (op == '-') {
                bil = bil.subtract(bir);
            } else if (op == '*') {
                bil = bil.multiply(bir);
            } else if (op == '/') {
                bil = bil.divide(bir);
            } else if (op == '%') {
                bil = bil.remainder(bir);
            }
            if (isLongExpression) {
                return new Long(bil.toString());
            } else {
                return new Integer(bil.toString());
            }
        } catch (ClassCastException e) {
            //
        } catch (ArithmeticException e) {
            /* probably a divide by zero */
            throw new SQLException(e.getMessage());
        }

        try {
            Number leftN = (Number) leftEval;
            BigDecimal bdl = new BigDecimal(leftN.toString());
            Number rightN = (Number) rightEval;
            BigDecimal bdr = new BigDecimal(rightN.toString());
            if (op == '+') {
                return new Double(bdl.add(bdr).toString());
            }
            if (op == '-') {
                return new Double(bdl.subtract(bdr).toString());
            }
            if (op == '*') {
                return new Double(bdl.multiply(bdr).toString());
            }
            MathContext mc = new MathContext("precision=14 roundingMode=HALF_UP");
            if (op == '/') {
                return new Double(bdl.divide(bdr, mc.getPrecision(), mc.getRoundingMode()).toString());
            }
            if (op == '%') {
                return new Double(bdl.remainder(bdr, mc).toString());
            }
        } catch (ClassCastException e) {
            //
        } catch (ArithmeticException e) {
			/* probably a divide by zero */
            throw new SQLException(e.getMessage());
        }

        try {
            if (op == '+' && leftEval instanceof Date) {
                Date leftD = (Date) leftEval;
                if (rightEval instanceof Time) {
                    Time rightT = (Time) rightEval;
                    Expression stringConverter = new ColumnName(StringConverter.COLUMN_NAME);
                    StringConverter sc = (StringConverter) stringConverter.eval(env);
                    return sc.parseTimestamp(leftD.toString() + " " + rightT.toString());
                } else {
                    Long rightLong;
                    if (rightEval instanceof Short) {
                        rightLong = ((Short) rightEval).longValue();
                    } else {
                        rightLong = (Long) rightEval;
                    }
                    return incrementDate(leftD, rightLong);
                }
            } else if (op == '+' && rightEval instanceof Date) {
                Date rightD = (Date) rightEval;
                if (leftEval instanceof Time) {
                    Time leftT = (Time) leftEval;
                    Expression stringConverter = new ColumnName(StringConverter.COLUMN_NAME);
                    StringConverter sc = (StringConverter) stringConverter.eval(env);
                    return sc.parseTimestamp(rightD.toString() + " " + leftT.toString());
                } else {
                    Long leftLong;
                    if (leftEval instanceof Short) {
                        leftLong = (Long) leftEval;
                    } else {
                        leftLong = (Long) leftEval;
                    }
                    return incrementDate(rightD, leftLong);
                }
            } else if (op == '-' && leftEval instanceof Date && rightEval instanceof Long) {
                return incrementDate((Date) leftEval, -((Long) rightEval).longValue());
            } else if (op == '-' && leftEval instanceof Date && rightEval instanceof Integer) {
                return incrementDate((Date) leftEval, -((Integer) rightEval).intValue());
            } else if (op == '-' && leftEval instanceof Date && rightEval instanceof Short) {
                return incrementDate((Date) leftEval, -((Short) rightEval).intValue());
            } else if (op == '-' && (leftEval instanceof Date || rightEval instanceof Date)) {
                if (!(leftEval instanceof Date)) {
                    Expression stringConverter = new ColumnName(StringConverter.COLUMN_NAME);
                    StringConverter sc = (StringConverter) stringConverter.eval(env);
                    leftEval = sc.parseDate(leftEval.toString());
                }
                if (!(rightEval instanceof Date)) {
                    Expression stringConverter = new ColumnName(StringConverter.COLUMN_NAME);
                    StringConverter sc = (StringConverter) stringConverter.eval(env);
                    rightEval = sc.parseDate(rightEval.toString());
                }
                if (leftEval != null && rightEval != null) {
                    long nMillis = ((Date) leftEval).getTime() - ((Date) (rightEval)).getTime();
                    long nDays = (nMillis + MILLISECONDS_PER_DAY / 2) / MILLISECONDS_PER_DAY;
                    return new Integer((int) nDays);
                }
            }
        } catch (ClassCastException e) {
        }
        try {
            if (op == '+' || op == '-') {
                Timestamp leftD = (Timestamp) leftEval;
                long time = leftD.getTime();
                Number rightN = (Number) rightEval;
                BigDecimal bdr = new BigDecimal(rightN.toString());
                if (op == '+') {
                    return new Timestamp(time + bdr.longValue());
                }
                if (op == '-') {
                    return new Timestamp(time - bdr.longValue());
                }
            }
        } catch (ClassCastException e) {
        }
        if (op == '+' || op == '|') {
            return "" + leftEval + rightEval;
        }
        return null;
    }

    private Date incrementDate(Date date, long nDays) {
        long newTime = date.getTime() +
                nDays * MILLISECONDS_PER_DAY + MILLISECONDS_PER_DAY / 2;
        Date newDate = new Date(newTime);
		/* Remove any time component from calculated date */
        newDate = Date.valueOf(newDate.toString());
        return newDate;
    }

    public String toString() {
        return "" + operation + " " + left + " " + right;
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
        /*
		 * An operation containing a logical expression such as (A > 5) + 1
		 * is not allowed in SQL.
		 */
        return !(left instanceof LogicalExpression ||
                right instanceof LogicalExpression) &&
                (left.isValid() && right.isValid());
    }
}
