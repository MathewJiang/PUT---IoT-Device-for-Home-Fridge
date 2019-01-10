package com.example.zhepingjiang.db;

import org.jsoup.Jsoup;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Vendors Table Unit Tests.
 */
public class VendorsTest {

    @Test
    public void testGetTableName_happyPath() {
        final String expectedTableName = "vendors";

        final String actualTableName = new Vendors().getTableName();

        assertEquals(expectedTableName, actualTableName);
    }

    @Test
    public void testGetInsertQuery_defaultVendor_happyPath() {
        final String expectedQuery = "insert into vendors values ('default');";

        final String actualQuery = new Vendors().getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetInsertQuery_arbitraryVendor_happyPath() {
        final String expectedQuery = "insert into vendors values ('Walmart');";

        final String actualQuery = new Vendors("Walmart").getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_defaultVendor_happyPath() {
        final String expectedQuery = "insert ignore into vendors values ('default');";

        final String actualQuery = new Vendors().getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_arbitraryVendor_happyPath() {
        final String expectedQuery = "insert ignore into vendors values ('Walmart');";

        final String actualQuery = new Vendors("Walmart").getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQuery_defaultVendor_happyPath() {
        final String expectedQuery = "delete from vendors where vendor_name = 'default';";

        final String actualQuery = new Vendors().getDeleteQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQuery_arbitraryVendor_happyPath() {
        final String expectedQuery = "delete from vendors where vendor_name = 'Walmart';";

        final String actualQuery = new Vendors("Walmart").getDeleteQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQuery_defaultVendor_happyPath() {
        final String expectedQuery = "select * from vendors where vendor_name = 'default';";

        final String actualQuery = new Vendors().getSelectQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQuery_arbitraryVendor_happyPath() {
        final String expectedQuery = "select * from vendors where vendor_name = 'Walmart';";

        final String actualQuery = new Vendors("Walmart").getSelectQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQueryStatic_happyPath() {
        final String expectedQuery = "delete from vendors where vendor_name = 'Loblaws';";

        final String actualQuery = Vendors.GetDeleteQueryStatic("Loblaws");

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQueryStatic_happyPath() {
        final String expectedQuery = "select * from vendors where vendor_name = 'Loblaws';";

        final String actualQuery = Vendors.GetSelectQueryStatic("Loblaws");

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectAllQueryStatic_happyPath() {
        final String expectedQuery = "select * from vendors;";

        final String actualQuery = Vendors.GetSelectAllQueryStatic();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testFromHTMLTableStr_singleEntry_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>vendor_name</TH></TR>" +
                "<TR><TD>Loblaws</TD></TR>" +
                "</TABLE>";

        final Set<Vendors> convertedObjs = Vendors.FromHTMLTableStr(htmlTableStr);

        assertEquals(1, convertedObjs.size());
        assertTrue(convertedObjs.contains(new Vendors("Loblaws")));
    }

    @Test
    public void testFromHTMLTableStr_multipleEntries_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>vendor_name</TH></TR>" +
                "<TR><TD>Loblaws</TD></TR>" +
                "<TR><TD>default</TD></TR>" +
                "<TR><TD>Lucky Moose</TD></TR>" +
                "</TABLE>";

        final Set<Vendors> convertedObjs = Vendors.FromHTMLTableStr(htmlTableStr);

        assertEquals(3, convertedObjs.size());
        assertTrue(convertedObjs.contains(new Vendors("Loblaws")));
        assertTrue(convertedObjs.contains(new Vendors("default")));
        assertTrue(convertedObjs.contains(new Vendors("Lucky Moose")));
    }

    @Test
    public void testFromHTMLTableStr_emptyTable_returnsEmptySet() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>vendor_name</TH></TR>" +
                "</TABLE>";

        final Set<Vendors> convertedObjs = Vendors.FromHTMLTableStr(htmlTableStr);

        assertTrue(convertedObjs.isEmpty());
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesAllValid_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>vendor_name</TH></TR>" +
                "<TR><TD>Loblaws</TD></TR>" +
                "<TR><TD>default</TD></TR>" +
                "</TABLE>" +
                "<TABLE BORDER=1>" +
                "<TR><TH>vendor_name</TH></TR>" +
                "<TR><TD>Lucky Moose</TD></TR>" +
                "</TABLE>";

        final Set<Vendors> convertedObjs = Vendors.FromHTMLTableStr(htmlTableStr);

        assertEquals(3, convertedObjs.size());
        assertTrue(convertedObjs.contains(new Vendors("Loblaws")));
        assertTrue(convertedObjs.contains(new Vendors("default")));
        assertTrue(convertedObjs.contains(new Vendors("Lucky Moose")));
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesOneValid_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>vendor_name</TH></TR>" +
                "<TR><TD>Loblaws</TD></TR>" +
                "<TR><TD>default</TD></TR>" +
                "</TABLE>" +
                "<TABLE BORDER=1>" +
                "<TR><TH>vendor_name</TH><TH>some_other_fields</TH></TR>" +
                "<TR><TD>Lucky Moose</TD><TD>blah1</TD></TR>" +
                "</TABLE>";

        final Set<Vendors> convertedObjs = Vendors.FromHTMLTableStr(htmlTableStr);

        assertEquals(2, convertedObjs.size());
        assertTrue(convertedObjs.contains(new Vendors("Loblaws")));
        assertTrue(convertedObjs.contains(new Vendors("default")));
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesNoneValid_returnsEmptyTable() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>vendor_name</TH><TH>some_other_fields</TH></TR>" +
                "<TR><TD>Loblaws</TD><TD>blah1</TD></TR>" +
                "<TR><TD>default</TD><TD>blah2</TD></TR>" +
                "</TABLE>" +
                "<TABLE BORDER=1>" +
                "<TR><TH>vendor_name</TH><TH>some_other_fields</TH></TR>" +
                "<TR><TD>Lucky Moose</TD><TD>blah3</TD></TR>" +
                "</TABLE>";

        final Set<Vendors> convertedObjs = Vendors.FromHTMLTableStr(htmlTableStr);

        assertTrue(convertedObjs.isEmpty());
    }

    @Test
    public void testIsValidTableSchema_schemaValid_returnsTrue() {
        final String validTableSchema = "<TABLE BORDER=1>" +
                "<TR><TH>vendor_name</TH></TR>" +
                "<TR><TD>Loblaws</TD></TR>" +
                "<TR><TD>default</TD></TR>" +
                "<TR><TD>Metro</TD></TR>" +
                "</TABLE>";

        final boolean isValidSchema = Vendors.isValidTableSchema(Jsoup.parse(validTableSchema));

        assertTrue(isValidSchema);
    }

    @Test
    public void testIsValidTableSchema_extraField_returnsFalse() {
        final String extraFieldTableSchema = "<TABLE BORDER=1>" +
                "<TR><TH>vendor_name</TH><TH>some_other_fields</TH></TR>" +
                "<TR><TD>Loblaws</TD><TD>blah1</TD></TR>" +
                "<TR><TD>default</TD><TD>blah2</TD></TR>" +
                "<TR><TD>Metro</TD><TD>blah3</TD></TR>" +
                "</TABLE>";

        final boolean isValidSchema = Vendors.isValidTableSchema(Jsoup.parse(extraFieldTableSchema));

        assertFalse(isValidSchema);
    }
}