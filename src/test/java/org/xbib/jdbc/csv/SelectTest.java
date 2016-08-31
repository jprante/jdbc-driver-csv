package org.xbib.jdbc.csv;

import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class SelectTest {

    private Connection connection;

    @Before
    public void setup() throws SQLException {
        connection = DriverManager.getConnection("jdbc:xbib:csv:src/test/resources/csv");
        System.setProperty("line.separator", "\n"); // used by PrintStream/BufferedWriter
    }

    @After
    public void close() throws SQLException {
        connection.close();
    }

    @Test
    public void testSelectByName() throws Exception {
        CsvStatement statement = (CsvStatement) connection.createStatement();
        String query = "SELECT PetalLength, Name FROM iris LIMIT 1";
        statement.execute(query);
        ResultSet resultSet = statement.getResultSet();
        assertEquals("PetalLength,Name\n1.4,setosa\n", resultSet);
    }

    @Test
    public void testSelectByAlias() throws Exception {
        CsvStatement statement = (CsvStatement) connection.createStatement();
        String query = "SELECT PetalLength AS myValueCol, Name as myName FROM iris LIMIT 1";
        statement.execute(query);
        ResultSet resultSet = statement.getResultSet();
        assertEquals("myValueCol,myName\n1.4,setosa\n", resultSet);
    }

    @Test
    public void testSelectByNameIncorrectCase() throws Exception {
        CsvStatement statement = (CsvStatement) connection.createStatement();
        String query = "SELECT name FROM iris LIMIT 1";
        statement.execute(query);
        ResultSet resultSet = statement.getResultSet();
        assertEquals("name\nsetosa\n", resultSet);
    }

    @Test
    public void testSelectByAliasIncorrectCase() throws Exception {
        CsvStatement statement = (CsvStatement) connection.createStatement();
        String query = "SELECT name as NAME FROM iris LIMIT 1";
        statement.execute(query);
        ResultSet resultSet = statement.getResultSet();
        assertEquals("NAME\nsetosa\n", resultSet);
    }

    private static void assertEquals(String text, ResultSet resultSet) throws SQLException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        CsvDriver.writeToCsv(resultSet, ps, true);
        assertArrayEquals(text.getBytes(), out.toByteArray());
    }
}
