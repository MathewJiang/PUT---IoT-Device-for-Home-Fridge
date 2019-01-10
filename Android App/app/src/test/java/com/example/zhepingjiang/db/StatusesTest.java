package com.example.zhepingjiang.db;

import org.jsoup.Jsoup;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Statuses Table Unit Tests.
 */
public class StatusesTest {

    @Test
    public void testGetTableName_happyPath() {
        final String expectedTableName = "statuses";

        final String actualTableName = new Statuses().getTableName();

        assertEquals(expectedTableName, actualTableName);
    }

    @Test
    public void testGetInsertQuery_defaultStatus_happyPath() {
        final String expectedQuery = "insert into statuses values ('default');";

        final String actualQuery = new Statuses().getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetInsertQuery_arbitraryStatus_happyPath() {
        final String expectedQuery = "insert into statuses values ('unopened');";

        final String actualQuery = new Statuses("unopened").getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_defaultStatus_happyPath() {
        final String expectedQuery = "insert ignore into statuses values ('default');";

        final String actualQuery = new Statuses().getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_arbitraryStatus_happyPath() {
        final String expectedQuery = "insert ignore into statuses values ('unopened');";

        final String actualQuery = new Statuses("unopened").getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQuery_defaultStatus_happyPath() {
        final String expectedQuery = "delete from statuses where status = 'default';";

        final String actualQuery = new Statuses().getDeleteQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQuery_arbitraryStatus_happyPath() {
        final String expectedQuery = "delete from statuses where status = 'unopened';";

        final String actualQuery = new Statuses("unopened").getDeleteQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQuery_defaultStatus_happyPath() {
        final String expectedQuery = "select * from statuses where status = 'default';";

        final String actualQuery = new Statuses().getSelectQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQuery_arbitraryStatus_happyPath() {
        final String expectedQuery = "select * from statuses where status = 'unopened';";

        final String actualQuery = new Statuses("unopened").getSelectQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQueryStatic_happyPath() {
        final String expectedQuery = "delete from statuses where status = 'good';";

        final String actualQuery = Statuses.GetDeleteQueryStatic("good");

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQueryStatic_happyPath() {
        final String expectedQuery = "select * from statuses where status = 'good';";

        final String actualQuery = Statuses.GetSelectQueryStatic("good");

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectAllQueryStatic_happyPath() {
        final String expectedQuery = "select * from statuses;";

        final String actualQuery = Statuses.GetSelectAllQueryStatic();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testFromHTMLTableStr_singleEntry_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>status</TH></TR>" +
                "<TR><TD>good</TD></TR>" +
                "</TABLE>";

        final Set<Statuses> convertedObjs = Statuses.FromHTMLTableStr(htmlTableStr);

        assertEquals(1, convertedObjs.size());
        assertTrue(convertedObjs.contains(new Statuses("good")));
    }

    @Test
    public void testFromHTMLTableStr_multipleEntries_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>status</TH></TR>" +
                "<TR><TD>good</TD></TR>" +
                "<TR><TD>default</TD></TR>" +
                "<TR><TD>disposed</TD></TR>" +
                "</TABLE>";

        final Set<Statuses> convertedObjs = Statuses.FromHTMLTableStr(htmlTableStr);

        assertEquals(3, convertedObjs.size());
        assertTrue(convertedObjs.contains(new Statuses("good")));
        assertTrue(convertedObjs.contains(new Statuses("default")));
        assertTrue(convertedObjs.contains(new Statuses("disposed")));
    }

    @Test
    public void testFromHTMLTableStr_emptyTable_returnsEmptySet() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>status</TH></TR>" +
                "</TABLE>";

        final Set<Statuses> convertedObjs = Statuses.FromHTMLTableStr(htmlTableStr);

        assertTrue(convertedObjs.isEmpty());
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesAllValid_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>status</TH></TR>" +
                "<TR><TD>good</TD></TR>" +
                "<TR><TD>default</TD></TR>" +
                "</TABLE>" +
                "<TABLE BORDER=1>" +
                "<TR><TH>status</TH></TR>" +
                "<TR><TD>disposed</TD></TR>" +
                "</TABLE>";

        final Set<Statuses> convertedObjs = Statuses.FromHTMLTableStr(htmlTableStr);

        assertEquals(3, convertedObjs.size());
        assertTrue(convertedObjs.contains(new Statuses("good")));
        assertTrue(convertedObjs.contains(new Statuses("default")));
        assertTrue(convertedObjs.contains(new Statuses("disposed")));
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesOneValid_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>status</TH></TR>" +
                "<TR><TD>good</TD></TR>" +
                "<TR><TD>default</TD></TR>" +
                "</TABLE>" +
                "<TABLE BORDER=1>" +
                "<TR><TH>status</TH><TH>some_other_fields</TH></TR>" +
                "<TR><TD>disposed</TD><TD>blah1</TD></TR>" +
                "</TABLE>";

        final Set<Statuses> convertedObjs = Statuses.FromHTMLTableStr(htmlTableStr);

        assertEquals(2, convertedObjs.size());
        assertTrue(convertedObjs.contains(new Statuses("good")));
        assertTrue(convertedObjs.contains(new Statuses("default")));
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesNoneValid_returnsEmptyTable() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>status</TH><TH>some_other_fields</TH></TR>" +
                "<TR><TD>good</TD><TD>blah1</TD></TR>" +
                "<TR><TD>default</TD><TD>blah2</TD></TR>" +
                "</TABLE>" +
                "<TABLE BORDER=1>" +
                "<TR><TH>status</TH><TH>some_other_fields</TH></TR>" +
                "<TR><TD>disposed</TD><TD>blah3</TD></TR>" +
                "</TABLE>";

        final Set<Statuses> convertedObjs = Statuses.FromHTMLTableStr(htmlTableStr);

        assertTrue(convertedObjs.isEmpty());
    }

    @Test
    public void testIsValidTableSchema_schemaValid_returnsTrue() {
        final String validTableSchema = "<TABLE BORDER=1>" +
                "<TR><TH>status</TH></TR>" +
                "<TR><TD>good</TD></TR>" +
                "<TR><TD>default</TD></TR>" +
                "<TR><TD>unopened</TD></TR>" +
                "</TABLE>";

        final boolean isValidSchema = Statuses.isValidTableSchema(Jsoup.parse(validTableSchema));

        assertTrue(isValidSchema);
    }

    @Test
    public void testIsValidTableSchema_extraField_returnsFalse() {
        final String extraFieldTableSchema = "<TABLE BORDER=1>" +
                "<TR><TH>status</TH><TH>some_other_fields</TH></TR>" +
                "<TR><TD>good</TD><TD>blah1</TD></TR>" +
                "<TR><TD>default</TD><TD>blah2</TD></TR>" +
                "<TR><TD>unopened</TD><TD>blah3</TD></TR>" +
                "</TABLE>";

        final boolean isValidSchema = Statuses.isValidTableSchema(Jsoup.parse(extraFieldTableSchema));

        assertFalse(isValidSchema);
    }
}