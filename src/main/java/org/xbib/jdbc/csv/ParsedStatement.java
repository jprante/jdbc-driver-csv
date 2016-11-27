package org.xbib.jdbc.csv;

import java.util.List;

/**
 *
 */
class ParsedStatement {
    List<ParsedExpression> queryEntries;
    boolean isDistinct;
    List<ParsedTable> tableEntries;
    ParsedExpression whereClause;
    List<ParsedExpression> groupByEntries;
    ParsedExpression havingClause;
    List<ParsedExpression> orderByEntries;
    int limit, offset;

    public ParsedStatement(List<ParsedExpression> queryEntries, boolean isDistinct,
                           List<ParsedTable> tableEntries,
                           ParsedExpression whereClause,
                           List<ParsedExpression> groupByEntries,
                           ParsedExpression havingClause,
                           List<ParsedExpression> orderByEntries,
                           int limit, int offset) {
        this.queryEntries = queryEntries;
        this.isDistinct = isDistinct;
        this.tableEntries = tableEntries;
        this.whereClause = whereClause;
        this.groupByEntries = groupByEntries;
        this.havingClause = havingClause;
        this.orderByEntries = orderByEntries;
        this.limit = limit;
        this.offset = offset;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT");
        if (isDistinct) {
            sb.append(" DISTINCT");
        }
        String separator = " ";
        for (ParsedExpression expr : queryEntries) {
            sb.append(separator);
            separator = ", ";
            QueryEnvEntry queryEnvEntry = (QueryEnvEntry) (expr.content);
            sb.append(queryEnvEntry.expression.toString());
        }
        if (tableEntries != null) {
            int tableCounter = 0;
            for (ParsedTable parsedTable : tableEntries) {
                if (tableCounter == 0) {
                    sb.append(" FROM");
                } else {
                    JoinType joinType = parsedTable.getJoinType();
                    if (joinType == JoinType.CROSS) {
                        sb.append(" CROSS JOIN");
                    } else if (joinType == JoinType.INNER) {
                        sb.append(" INNER JOIN");
                    } else if (joinType == JoinType.LEFT_OUTER) {
                        sb.append(" LEFT OUTER JOIN");
                    } else if (joinType == JoinType.RIGHT_OUTER) {
                        sb.append(" RIGHT OUTER JOIN");
                    } else if (joinType == JoinType.FULL_OUTER) {
                        sb.append(" FULL OUTER JOIN");
                    }
                }
                sb.append(" ");
                sb.append(parsedTable.getTableName());
                String tableAlias = parsedTable.getTableAlias();
                if (tableAlias != null) {
                    sb.append(" ");
                    sb.append(tableAlias);
                }

                if (tableCounter > 0 && parsedTable.getJoinType() != JoinType.CROSS) {
                    sb.append(" ON ");
                    sb.append(parsedTable.getJoinClause().toString());
                }
                tableCounter++;
            }

            if (whereClause != null) {
                sb.append(" WHERE ").append(whereClause.toString());
            }

            if (groupByEntries != null && groupByEntries.size() > 0) {
                sb.append(" GROUP BY");
                separator = " ";
                for (ParsedExpression expr : groupByEntries) {
                    sb.append(separator);
                    separator = ", ";
                    sb.append(expr.toString());
                }
                if (havingClause != null) {
                    sb.append(" HAVING ");
                    sb.append(havingClause.toString());
                }
            }

            if (orderByEntries != null && orderByEntries.size() > 0) {
                sb.append(" ORDER BY");
                separator = " ";
                for (ParsedExpression expr : orderByEntries) {
                    sb.append(separator);
                    separator = ", ";
                    sb.append(expr.toString());
                }
            }
        }

        if (limit >= 0) {
            sb.append(" LIMIT ").append(limit);
        }
        if (offset > 0) {
            sb.append(" OFFSET ").append(offset);
        }
        return sb.toString();
    }
}