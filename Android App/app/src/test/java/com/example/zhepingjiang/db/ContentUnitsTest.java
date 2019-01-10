package com.example.zhepingjiang.db;

import org.jsoup.Jsoup;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Content Units Table Unit Tests.
 */
public class ContentUnitsTest {

    @Test
    public void testGetTableName_happyPath() {
        final String expectedTableName = "content_units";

        final String actualTableName = new ContentUnits().getTableName();

        assertEquals(expectedTableName, actualTableName);
    }

    @Test
    public void testGetInsertQuery_defaultUnit_happyPath() {
        final String expectedQuery = "insert into content_units values ('default');";

        final String actualQuery = new ContentUnits().getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetInsertQuery_arbitraryUnit_happyPath() {
        final String expectedQuery = "insert into content_units values ('ml');";

        final String actualQuery = new ContentUnits("ml").getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_defaultUnit_happyPath() {
        final String expectedQuery = "insert ignore into content_units values ('default');";

        final String actualQuery = new ContentUnits().getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_arbitraryUnit_happyPath() {
        final String expectedQuery = "insert ignore into content_units values ('ml');";

        final String actualQuery = new ContentUnits("ml").getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQuery_defaultUnit_happyPath() {
        final String expectedQuery = "delete from content_units where unit = 'default';";

        final String actualQuery = new ContentUnits().getDeleteQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQuery_arbitraryUnit_happyPath() {
        final String expectedQuery = "delete from content_units where unit = 'ml';";

        final String actualQuery = new ContentUnits("ml").getDeleteQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQuery_defaultUnit_happyPath() {
        final String expectedQuery = "select * from content_units where unit = 'default';";

        final String actualQuery = new ContentUnits().getSelectQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQuery_arbitraryUnit_happyPath() {
        final String expectedQuery = "select * from content_units where unit = 'ml';";

        final String actualQuery = new ContentUnits("ml").getSelectQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQueryStatic_happyPath() {
        final String expectedQuery = "delete from content_units where unit = 'ml';";

        final String actualQuery = ContentUnits.GetDeleteQueryStatic("ml");

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQueryStatic_happyPath() {
        final String expectedQuery = "select * from content_units where unit = 'ml';";

        final String actualQuery = ContentUnits.GetSelectQueryStatic("ml");

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectAllQueryStatic_happyPath() {
        final String expectedQuery = "select * from content_units;";

        final String actualQuery = ContentUnits.GetSelectAllQueryStatic();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testFromHTMLTableStr_singleEntry_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>unit</TH></TR>" +
                "<TR><TD>lb</TD></TR>" +
                "</TABLE>";

        final Set<ContentUnits> convertedObjs = ContentUnits.FromHTMLTableStr(htmlTableStr);

        assertEquals(1, convertedObjs.size());
        assertTrue(convertedObjs.contains(new ContentUnits("lb")));
    }

    @Test
    public void testFromHTMLTableStr_multipleEntries_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>unit</TH></TR>" +
                "<TR><TD>lb</TD></TR>" +
                "<TR><TD>default</TD></TR>" +
                "<TR><TD>kg</TD></TR>" +
                "</TABLE>";

        final Set<ContentUnits> convertedObjs = ContentUnits.FromHTMLTableStr(htmlTableStr);

        assertEquals(3, convertedObjs.size());
        assertTrue(convertedObjs.contains(new ContentUnits("lb")));
        assertTrue(convertedObjs.contains(new ContentUnits("default")));
        assertTrue(convertedObjs.contains(new ContentUnits("kg")));
    }

    @Test
    public void testFromHTMLTableStr_emptyTable_returnsEmptySet() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>unit</TH></TR>" +
                "</TABLE>";

        final Set<ContentUnits> convertedObjs = ContentUnits.FromHTMLTableStr(htmlTableStr);

        assertTrue(convertedObjs.isEmpty());
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesAllValid_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>unit</TH></TR>" +
                "<TR><TD>lb</TD></TR>" +
                "<TR><TD>default</TD></TR>" +
                "</TABLE>" +
                "<TABLE BORDER=1>" +
                "<TR><TH>unit</TH></TR>" +
                "<TR><TD>kg</TD></TR>" +
                "</TABLE>";

        final Set<ContentUnits> convertedObjs = ContentUnits.FromHTMLTableStr(htmlTableStr);

        assertEquals(3, convertedObjs.size());
        assertTrue(convertedObjs.contains(new ContentUnits("lb")));
        assertTrue(convertedObjs.contains(new ContentUnits("default")));
        assertTrue(convertedObjs.contains(new ContentUnits("kg")));
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesOneValid_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>unit</TH></TR>" +
                "<TR><TD>lb</TD></TR>" +
                "<TR><TD>default</TD></TR>" +
                "</TABLE>" +
                "<TABLE BORDER=1>" +
                "<TR><TH>unit</TH><TH>some_other_fields</TH></TR>" +
                "<TR><TD>kg</TD><TD>blah1</TD></TR>" +
                "</TABLE>";

        final Set<ContentUnits> convertedObjs = ContentUnits.FromHTMLTableStr(htmlTableStr);

        assertEquals(2, convertedObjs.size());
        assertTrue(convertedObjs.contains(new ContentUnits("lb")));
        assertTrue(convertedObjs.contains(new ContentUnits("default")));
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesNoneValid_returnsEmptyTable() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>unit</TH><TH>some_other_fields</TH></TR>" +
                "<TR><TD>lb</TD><TD>blah1</TD></TR>" +
                "<TR><TD>default</TD><TD>blah2</TD></TR>" +
                "</TABLE>" +
                "<TABLE BORDER=1>" +
                "<TR><TH>unit</TH><TH>some_other_fields</TH></TR>" +
                "<TR><TD>kg</TD><TD>blah3</TD></TR>" +
                "</TABLE>";

        final Set<ContentUnits> convertedObjs = ContentUnits.FromHTMLTableStr(htmlTableStr);

        assertTrue(convertedObjs.isEmpty());
    }

    @Test
    public void testIsValidTableSchema_schemaValid_returnsTrue() {
        final String validTableSchema = "<TABLE BORDER=1>" +
                "<TR><TH>unit</TH></TR>" +
                "<TR><TD>lb</TD></TR>" +
                "<TR><TD>default</TD></TR>" +
                "<TR><TD>oz</TD></TR>" +
                "</TABLE>";

        final boolean isValidSchema = ContentUnits.isValidTableSchema(Jsoup.parse(validTableSchema));

        assertTrue(isValidSchema);
    }

    @Test
    public void testIsValidTableSchema_extraField_returnsFalse() {
        final String extraFieldTableSchema = "<TABLE BORDER=1>" +
                "<TR><TH>unit</TH><TH>some_other_fields</TH></TR>" +
                "<TR><TD>lb</TD><TD>blah1</TD></TR>" +
                "<TR><TD>default</TD><TD>blah2</TD></TR>" +
                "<TR><TD>oz</TD><TD>blah3</TD></TR>" +
                "</TABLE>";

        final boolean isValidSchema = ContentUnits.isValidTableSchema(Jsoup.parse(extraFieldTableSchema));

        assertFalse(isValidSchema);
    }
}