package org.xbib.jdbc.csv;

import java.io.StringReader;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Parses one or many SQL statements.
 */
public class MultipleSqlParser {
    public List<SqlParser> parse(String sql) throws ParseException, SQLException {
        // Ensure last line of SQL statement ends with newline so we can
        // correctly skip single-line comments.
        sql = sql + "\n";

        ExpressionParser cs2 = new ExpressionParser(new StringReader(sql));
        List<ParsedStatement> statements = cs2.parseMultipleStatements();
        LinkedList<SqlParser> retval = new LinkedList<SqlParser>();
        for (ParsedStatement parsedStatement : statements) {
            SqlParser sqlParser = new SqlParser();
            sqlParser.setParsedStatement(parsedStatement);
            retval.add(sqlParser);
        }
        return retval;
    }
}
