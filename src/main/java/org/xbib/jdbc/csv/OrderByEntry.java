package org.xbib.jdbc.csv;

/**
 *
 */
class OrderByEntry extends Expression {
    String order;
    Expression expression;

    public OrderByEntry(Expression expression, String order) {
        this.order = order.toUpperCase();
        this.expression = expression;
    }

    public String toString() {
        return expression.toString() + " " + order;
    }
}