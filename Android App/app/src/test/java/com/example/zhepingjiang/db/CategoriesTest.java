package com.example.zhepingjiang.db;

import org.jsoup.Jsoup;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Categories Table Unit Tests.
 */
public class CategoriesTest {

    @Test
    public void testGetTableName_happyPath() {
        final String expectedTableName = "categories";

        final String actualTableName = new Categories().getTableName();

        assertEquals(expectedTableName, actualTableName);
    }

    @Test
    public void testGetInsertQuery_defaultCategory_happyPath() {
        final String expectedQuery = "insert into categories values ('uncategorized');";

        final String actualQuery = new Categories().getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetInsertQuery_arbitraryCategory_happyPath() {
        final String expectedQuery = "insert into categories values ('canned food');";

        final String actualQuery = new Categories("canned food").getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_defaultCategory_happyPath() {
        final String expectedQuery = "insert ignore into categories values ('uncategorized');";

        final String actualQuery = new Categories().getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_arbitraryCategory_happyPath() {
        final String expectedQuery = "insert ignore into categories values ('canned food');";

        final String actualQuery = new Categories("canned food").getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQuery_defaultCategory_happyPath() {
        final String expectedQuery = "delete from categories where category = 'uncategorized';";

        final String actualQuery = new Categories().getDeleteQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQuery_arbitraryCategory_happyPath() {
        final String expectedQuery = "delete from categories where category = 'canned food';";

        final String actualQuery = new Categories("canned food").getDeleteQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQuery_defaultCategory_happyPath() {
        final String expectedQuery = "select * from categories where category = 'uncategorized';";

        final String actualQuery = new Categories().getSelectQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQuery_arbitraryCategory_happyPath() {
        final String expectedQuery = "select * from categories where category = 'canned food';";

        final String actualQuery = new Categories("canned food").getSelectQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQueryStatic_happyPath() {
        final String expectedQuery = "delete from categories where category = 'drink';";

        final String actualQuery = Categories.GetDeleteQueryStatic("drink");

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQueryStatic_happyPath() {
        final String expectedQuery = "select * from categories where category = 'drink';";

        final String actualQuery = Categories.GetSelectQueryStatic("drink");

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectAllQueryStatic_happyPath() {
        final String expectedQuery = "select * from categories;";

        final String actualQuery = Categories.GetSelectAllQueryStatic();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testFromHTMLTableStr_singleEntry_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>category</TH></TR>" +
                "<TR><TD>drink</TD></TR>" +
                "</TABLE>";

        final Set<Categories> convertedObjs = Categories.FromHTMLTableStr(htmlTableStr);

        assertEquals(1, convertedObjs.size());
        assertTrue(convertedObjs.contains(new Categories("drink")));
    }

    @Test
    public void testFromHTMLTableStr_multipleEntries_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>category</TH></TR>" +
                "<TR><TD>drink</TD></TR>" +
                "<TR><TD>uncategorized</TD></TR>" +
                "<TR><TD>egg</TD></TR>" +
                "</TABLE>";

        final Set<Categories> convertedObjs = Categories.FromHTMLTableStr(htmlTableStr);

        assertEquals(3, convertedObjs.size());
        assertTrue(convertedObjs.contains(new Categories("drink")));
        assertTrue(convertedObjs.contains(new Categories("uncategorized")));
        assertTrue(convertedObjs.contains(new Categories("egg")));
    }

    @Test
    public void testFromHTMLTableStr_emptyTable_returnsEmptySet() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>category</TH></TR>" +
                "</TABLE>";

        final Set<Categories> convertedObjs = Categories.FromHTMLTableStr(htmlTableStr);

        assertTrue(convertedObjs.isEmpty());
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesAllValid_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>category</TH></TR>" +
                "<TR><TD>drink</TD></TR>" +
                "<TR><TD>uncategorized</TD></TR>" +
                "</TABLE>" +
                "<TABLE BORDER=1>" +
                "<TR><TH>category</TH></TR>" +
                "<TR><TD>egg</TD></TR>" +
                "</TABLE>";

        final Set<Categories> convertedObjs = Categories.FromHTMLTableStr(htmlTableStr);

        assertEquals(3, convertedObjs.size());
        assertTrue(convertedObjs.contains(new Categories("drink")));
        assertTrue(convertedObjs.contains(new Categories("uncategorized")));
        assertTrue(convertedObjs.contains(new Categories("egg")));
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesOneValid_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>category</TH></TR>" +
                "<TR><TD>drink</TD></TR>" +
                "<TR><TD>uncategorized</TD></TR>" +
                "</TABLE>" +
                "<TABLE BORDER=1>" +
                "<TR><TH>category</TH><TH>some_other_fields</TH></TR>" +
                "<TR><TD>egg</TD><TD>blah1</TD></TR>" +
                "</TABLE>";

        final Set<Categories> convertedObjs = Categories.FromHTMLTableStr(htmlTableStr);

        assertEquals(2, convertedObjs.size());
        assertTrue(convertedObjs.contains(new Categories("drink")));
        assertTrue(convertedObjs.contains(new Categories("uncategorized")));
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesNoneValid_returnsEmptyTable() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>category</TH><TH>some_other_fields</TH></TR>" +
                "<TR><TD>drink</TD><TD>blah1</TD></TR>" +
                "<TR><TD>uncategorized</TD><TD>blah2</TD></TR>" +
                "</TABLE>" +
                "<TABLE BORDER=1>" +
                "<TR><TH>category</TH><TH>some_other_fields</TH></TR>" +
                "<TR><TD>egg</TD><TD>blah3</TD></TR>" +
                "</TABLE>";

        final Set<Categories> convertedObjs = Categories.FromHTMLTableStr(htmlTableStr);

        assertTrue(convertedObjs.isEmpty());
    }

    @Test
    public void testIsValidTableSchema_schemaValid_returnsTrue() {
        final String validTableSchema = "<TABLE BORDER=1>" +
                "<TR><TH>category</TH></TR>" +
                "<TR><TD>drink</TD></TR>" +
                "<TR><TD>uncategorized</TD></TR>" +
                "<TR><TD>egg</TD></TR>" +
                "</TABLE>";

        final boolean isValidSchema = Categories.isValidTableSchema(Jsoup.parse(validTableSchema));

        assertTrue(isValidSchema);
    }

    @Test
    public void testIsValidTableSchema_extraField_returnsFalse() {
        final String extraFieldTableSchema = "<TABLE BORDER=1>" +
                "<TR><TH>category</TH><TH>some_other_fields</TH></TR>" +
                "<TR><TD>drink</TD><TD>blah1</TD></TR>" +
                "<TR><TD>uncategorized</TD><TD>blah2</TD></TR>" +
                "<TR><TD>egg</TD><TD>blah3</TD></TR>" +
                "</TABLE>";

        final boolean isValidSchema = Categories.isValidTableSchema(Jsoup.parse(extraFieldTableSchema));

        assertFalse(isValidSchema);
    }
}