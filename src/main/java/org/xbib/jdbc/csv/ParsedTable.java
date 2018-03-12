package org.xbib.jdbc.csv;

/**
 *
 */
class ParsedTable {
    private JoinType joinType;
    private LogicalExpression joinClause;
    private String tableName;
    private String tableAlias;

    public ParsedTable(String tableName, String tableAlias) {
        this.joinType = JoinType.NONE;
        this.joinClause = null;
        this.tableName = tableName.replace(CsvDatabaseMetaData.SCHEMA_NAME + ".", "");
        this.tableAlias = tableAlias;
    }

    public ParsedTable(JoinType joinType, LogicalExpression joinClause,
                       String tableName, String tableAlias) {
        this.joinType = joinType;
        this.joinClause = joinClause;
        this.tableName = tableName.replace(CsvDatabaseMetaData.SCHEMA_NAME + ".", "");
        this.tableAlias = tableAlias;
    }

    public JoinType getJoinType() {
        return joinType;
    }

    public LogicalExpression getJoinClause() {
        return joinClause;
    }

    public String getTableName() {
        return tableName;
    }

    public String getTableAlias() {
        return tableAlias;
    }
}
