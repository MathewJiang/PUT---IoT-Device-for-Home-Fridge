package com.example.zhepingjiang.db;

import org.jsoup.Jsoup;
import org.junit.Test;
import org.junit.Ignore;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Actions Table Unit Tests.
 */
public class ActionsTest {
    
    @Test
    public void testGetTableName_happyPath() {
        final String expectedTableName = "actions";

        final String actualTableName = new Actions().getTableName();

        assertEquals(expectedTableName, actualTableName);
    }

    @Test
    public void testGetInsertQuery_defaultAction_happyPath() {
        final String expectedQuery = "insert into actions values ('default');";

        final String actualQuery = new Actions().getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetInsertQuery_arbitraryAction_happyPath() {
        final String expectedQuery = "insert into actions values ('retain');";

        final String actualQuery = new Actions("retain").getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_defaultAction_happyPath() {
        final String expectedQuery = "insert ignore into actions values ('default');";

        final String actualQuery = new Actions().getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_arbitraryAction_happyPath() {
        final String expectedQuery = "insert ignore into actions values ('retain');";

        final String actualQuery = new Actions("retain").getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQuery_defaultAction_happyPath() {
        final String expectedQuery = "delete from actions where user_action = 'default';";

        final String actualQuery = new Actions().getDeleteQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQuery_arbitraryAction_happyPath() {
        final String expectedQuery = "delete from actions where user_action = 'retain';";

        final String actualQuery = new Actions("retain").getDeleteQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQuery_defaultAction_happyPath() {
        final String expectedQuery = "select * from actions where user_action = 'default';";

        final String actualQuery = new Actions().getSelectQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQuery_arbitraryAction_happyPath() {
        final String expectedQuery = "select * from actions where user_action = 'retain';";

        final String actualQuery = new Actions("retain").getSelectQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDelectQueryStatic_happyPath() {
        final String expectedQuery = "delete from actions where user_action = 'retain';";

        final String actualQuery = Actions.GetDeleteQueryStatic("retain");

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQueryStatic_happyPath() {
        final String expectedQuery = "select * from actions where user_action = 'retain';";

        final String actualQuery = Actions.GetSelectQueryStatic("retain");

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectAllQueryStatic_happyPath() {
        final String expectedQuery = "select * from actions;";

        final String actualQuery = Actions.GetSelectAllQueryStatic();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testFromHTMLTableStr_singleEntry_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>user_action</TH></TR>" +
                "<TR><TD>consumption</TD></TR>" +
                "</TABLE>";

        final Set<Actions> convertedObjs = Actions.FromHTMLTableStr(htmlTableStr);

        assertEquals(1, convertedObjs.size());
        assertTrue(convertedObjs.contains(new Actions("consumption")));
    }

    @Test
    public void testFromHTMLTableStr_multipleEntries_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>user_action</TH></TR>" +
                "<TR><TD>consumption</TD></TR>" +
                "<TR><TD>default</TD></TR>" +
                "<TR><TD>disposal</TD></TR>" +
                "</TABLE>";

        final Set<Actions> convertedObjs = Actions.FromHTMLTableStr(htmlTableStr);

        assertEquals(3, convertedObjs.size());
        assertTrue(convertedObjs.contains(new Actions("consumption")));
        assertTrue(convertedObjs.contains(new Actions("default")));
        assertTrue(convertedObjs.contains(new Actions("disposal")));
    }

    @Test
    public void testFromHTMLTableStr_emptyTable_returnsEmptySet() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>user_action</TH></TR>" +
                "</TABLE>";

        final Set<Actions> convertedObjs = Actions.FromHTMLTableStr(htmlTableStr);

        assertTrue(convertedObjs.isEmpty());
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesAllValid_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>user_action</TH></TR>" +
                "<TR><TD>consumption</TD></TR>" +
                "<TR><TD>default</TD></TR>" +
                "</TABLE>" +
                "<TABLE BORDER=1>" +
                "<TR><TH>user_action</TH></TR>" +
                "<TR><TD>disposal</TD></TR>" +
                "</TABLE>";

        final Set<Actions> convertedObjs = Actions.FromHTMLTableStr(htmlTableStr);

        assertEquals(3, convertedObjs.size());
        assertTrue(convertedObjs.contains(new Actions("consumption")));
        assertTrue(convertedObjs.contains(new Actions("default")));
        assertTrue(convertedObjs.contains(new Actions("disposal")));
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesOneValid_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>user_action</TH></TR>" +
                "<TR><TD>consumption</TD></TR>" +
                "<TR><TD>default</TD></TR>" +
                "</TABLE>" +
                "<TABLE BORDER=1>" +
                "<TR><TH>user_action</TH><TH>some_other_fields</TH></TR>" +
                "<TR><TD>disposal</TD><TD>blah1</TD></TR>" +
                "</TABLE>";

        final Set<Actions> convertedObjs = Actions.FromHTMLTableStr(htmlTableStr);

        assertEquals(2, convertedObjs.size());
        assertTrue(convertedObjs.contains(new Actions("consumption")));
        assertTrue(convertedObjs.contains(new Actions("default")));
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesNoneValid_returnsEmptyTable() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>user_action</TH><TH>some_other_fields</TH></TR>" +
                "<TR><TD>consumption</TD><TD>blah1</TD></TR>" +
                "<TR><TD>default</TD><TD>blah2</TD></TR>" +
                "</TABLE>" +
                "<TABLE BORDER=1>" +
                "<TR><TH>user_action</TH><TH>some_other_fields</TH></TR>" +
                "<TR><TD>disposal</TD><TD>blah3</TD></TR>" +
                "</TABLE>";

        final Set<Actions> convertedObjs = Actions.FromHTMLTableStr(htmlTableStr);

        assertTrue(convertedObjs.isEmpty());
    }

    @Test
    public void testIsValidTableSchema_schemaValid_returnsTrue() {
        final String validTableSchema = "<TABLE BORDER=1>" +
                "<TR><TH>user_action</TH></TR>" +
                "<TR><TD>consumption</TD></TR>" +
                "<TR><TD>default</TD></TR>" +
                "<TR><TD>disposal</TD></TR>" +
                "</TABLE>";

        final boolean isValidSchema = Actions.isValidTableSchema(Jsoup.parse(validTableSchema));

        assertTrue(isValidSchema);
    }

    @Test
    public void testIsValidTableSchema_extraField_returnsFalse() {
        final String extraFieldTableSchema = "<TABLE BORDER=1>" +
                "<TR><TH>user_action</TH><TH>some_other_fields</TH></TR>" +
                "<TR><TD>consumption</TD><TD>blah1</TD></TR>" +
                "<TR><TD>default</TD><TD>blah2</TD></TR>" +
                "<TR><TD>disposal</TD><TD>blah3</TD></TR>" +
                "</TABLE>";

        final boolean isValidSchema = Actions.isValidTableSchema(Jsoup.parse(extraFieldTableSchema));

        assertFalse(isValidSchema);
    }
}