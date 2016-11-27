package org.xbib.jdbc.csv;

/**
 * Types of SQL joins between tables.
 */
public enum JoinType {
    NONE,
    CROSS,
    INNER,
    LEFT_OUTER,
    RIGHT_OUTER,
    FULL_OUTER
}
