package com.example.zhepingjiang.db;

import org.jsoup.Jsoup;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Package Units Table Unit Tests.
 */
public class PackageUnitsTest {

    @Test
    public void testGetTableName_happyPath() {
        final String expectedTableName = "package_units";

        final String actualTableName = new PackageUnits().getTableName();

        assertEquals(expectedTableName, actualTableName);
    }

    @Test
    public void testGetInsertQuery_defaultUnit_happyPath() {
        final String expectedQuery = "insert into package_units values ('default');";

        final String actualQuery = new PackageUnits().getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetInsertQuery_arbitraryUnit_happyPath() {
        final String expectedQuery = "insert into package_units values ('bottle');";

        final String actualQuery = new PackageUnits("bottle").getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_defaultUnit_happyPath() {
        final String expectedQuery = "insert ignore into package_units values ('default');";

        final String actualQuery = new PackageUnits().getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_arbitraryUnit_happyPath() {
        final String expectedQuery = "insert ignore into package_units values ('bottle');";

        final String actualQuery = new PackageUnits("bottle").getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQuery_defaultUnit_happyPath() {
        final String expectedQuery = "delete from package_units where unit = 'default';";

        final String actualQuery = new PackageUnits().getDeleteQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQuery_arbitraryUnit_happyPath() {
        final String expectedQuery = "delete from package_units where unit = 'bottle';";

        final String actualQuery = new PackageUnits("bottle").getDeleteQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQuery_defaultUnit_happyPath() {
        final String expectedQuery = "select * from package_units where unit = 'default';";

        final String actualQuery = new PackageUnits().getSelectQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQuery_arbitraryUnit_happyPath() {
        final String expectedQuery = "select * from package_units where unit = 'bottle';";

        final String actualQuery = new PackageUnits("bottle").getSelectQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQueryStatic_happyPath() {
        final String expectedQuery = "delete from package_units where unit = 'bottle';";

        final String actualQuery = PackageUnits.GetDeleteQueryStatic("bottle");

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQueryStatic_happyPath() {
        final String expectedQuery = "select * from package_units where unit = 'bottle';";

        final String actualQuery = PackageUnits.GetSelectQueryStatic("bottle");

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectAllQueryStatic_happyPath() {
        final String expectedQuery = "select * from package_units;";

        final String actualQuery = PackageUnits.GetSelectAllQueryStatic();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testFromHTMLTableStr_singleEntry_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>unit</TH></TR>" +
                "<TR><TD>box</TD></TR>" +
                "</TABLE>";

        final Set<PackageUnits> convertedObjs = PackageUnits.FromHTMLTableStr(htmlTableStr);

        assertEquals(1, convertedObjs.size());
        assertTrue(convertedObjs.contains(new PackageUnits("box")));
    }

    @Test
    public void testFromHTMLTableStr_multipleEntries_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>unit</TH></TR>" +
                "<TR><TD>box</TD></TR>" +
                "<TR><TD>default</TD></TR>" +
                "<TR><TD>can</TD></TR>" +
                "</TABLE>";

        final Set<PackageUnits> convertedObjs = PackageUnits.FromHTMLTableStr(htmlTableStr);

        assertEquals(3, convertedObjs.size());
        assertTrue(convertedObjs.contains(new PackageUnits("box")));
        assertTrue(convertedObjs.contains(new PackageUnits("default")));
        assertTrue(convertedObjs.contains(new PackageUnits("can")));
    }

    @Test
    public void testFromHTMLTableStr_emptyTable_returnsEmptySet() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>unit</TH></TR>" +
                "</TABLE>";

        final Set<PackageUnits> convertedObjs = PackageUnits.FromHTMLTableStr(htmlTableStr);

        assertTrue(convertedObjs.isEmpty());
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesAllValid_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>unit</TH></TR>" +
                "<TR><TD>box</TD></TR>" +
                "<TR><TD>default</TD></TR>" +
                "</TABLE>" +
                "<TABLE BORDER=1>" +
                "<TR><TH>unit</TH></TR>" +
                "<TR><TD>can</TD></TR>" +
                "</TABLE>";

        final Set<PackageUnits> convertedObjs = PackageUnits.FromHTMLTableStr(htmlTableStr);

        assertEquals(3, convertedObjs.size());
        assertTrue(convertedObjs.contains(new PackageUnits("box")));
        assertTrue(convertedObjs.contains(new PackageUnits("default")));
        assertTrue(convertedObjs.contains(new PackageUnits("can")));
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesOneValid_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>unit</TH></TR>" +
                "<TR><TD>box</TD></TR>" +
                "<TR><TD>default</TD></TR>" +
                "</TABLE>" +
                "<TABLE BORDER=1>" +
                "<TR><TH>unit</TH><TH>some_other_fields</TH></TR>" +
                "<TR><TD>can</TD><TD>blah1</TD></TR>" +
                "</TABLE>";

        final Set<PackageUnits> convertedObjs = PackageUnits.FromHTMLTableStr(htmlTableStr);

        assertEquals(2, convertedObjs.size());
        assertTrue(convertedObjs.contains(new PackageUnits("box")));
        assertTrue(convertedObjs.contains(new PackageUnits("default")));
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesNoneValid_returnsEmptyTable() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>unit</TH><TH>some_other_fields</TH></TR>" +
                "<TR><TD>box</TD><TD>blah1</TD></TR>" +
                "<TR><TD>default</TD><TD>blah2</TD></TR>" +
                "</TABLE>" +
                "<TABLE BORDER=1>" +
                "<TR><TH>unit</TH><TH>some_other_fields</TH></TR>" +
                "<TR><TD>can</TD><TD>blah3</TD></TR>" +
                "</TABLE>";

        final Set<PackageUnits> convertedObjs = PackageUnits.FromHTMLTableStr(htmlTableStr);

        assertTrue(convertedObjs.isEmpty());
    }

    @Test
    public void testIsValidTableSchema_schemaValid_returnsTrue() {
        final String validTableSchema = "<TABLE BORDER=1>" +
                "<TR><TH>unit</TH></TR>" +
                "<TR><TD>box</TD></TR>" +
                "<TR><TD>default</TD></TR>" +
                "<TR><TD>bag</TD></TR>" +
                "</TABLE>";

        final boolean isValidSchema = PackageUnits.isValidTableSchema(Jsoup.parse(validTableSchema));

        assertTrue(isValidSchema);
    }

    @Test
    public void testIsValidTableSchema_extraField_returnsFalse() {
        final String extraFieldTableSchema = "<TABLE BORDER=1>" +
                "<TR><TH>unit</TH><TH>some_other_fields</TH></TR>" +
                "<TR><TD>box</TD><TD>blah1</TD></TR>" +
                "<TR><TD>default</TD><TD>blah2</TD></TR>" +
                "<TR><TD>bag</TD><TD>blah3</TD></TR>" +
                "</TABLE>";

        final boolean isValidSchema = PackageUnits.isValidTableSchema(Jsoup.parse(extraFieldTableSchema));

        assertFalse(isValidSchema);
    }
}