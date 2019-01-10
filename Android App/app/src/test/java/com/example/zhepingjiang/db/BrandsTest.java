package com.example.zhepingjiang.db;

import org.jsoup.Jsoup;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Brands Table Unit Tests.
 */
public class BrandsTest {

    @Test
    public void testGetTableName_happyPath() {
        final String expectedTableName = "brands";

        final String actualTableName = new Brands().getTableName();

        assertEquals(expectedTableName, actualTableName);
    }

    @Test
    public void testGetInsertQuery_defaultBrand_happyPath() {
        final String expectedQuery = "insert into brands values ('default');";

        final String actualQuery = new Brands().getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetInsertQuery_arbitraryBrand_happyPath() {
        final String expectedQuery = "insert into brands values ('luobawang');";

        final String actualQuery = new Brands("luobawang").getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_defaultBrand_happyPath() {
        final String expectedQuery = "insert ignore into brands values ('default');";

        final String actualQuery = new Brands().getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_arbitraryBrand_happyPath() {
        final String expectedQuery = "insert ignore into brands values ('luobawang');";

        final String actualQuery = new Brands("luobawang").getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQuery_defaultBrand_happyPath() {
        final String expectedQuery = "delete from brands where brand_name = 'default';";

        final String actualQuery = new Brands().getDeleteQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQuery_arbitraryBrand_happyPath() {
        final String expectedQuery = "delete from brands where brand_name = 'luobawang';";

        final String actualQuery = new Brands("luobawang").getDeleteQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQuery_defaultBrand_happyPath() {
        final String expectedQuery = "select * from brands where brand_name = 'default';";

        final String actualQuery = new Brands().getSelectQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQuery_arbitraryBrand_happyPath() {
        final String expectedQuery = "select * from brands where brand_name = 'luobawang';";

        final String actualQuery = new Brands("luobawang").getSelectQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQueryStatic_happyPath() {
        final String expectedQuery = "delete from brands where brand_name = 'luobawang';";

        final String actualQuery = Brands.GetDeleteQueryStatic("luobawang");

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQueryStatic_happyPath() {
        final String expectedQuery = "select * from brands where brand_name = 'luobawang';";

        final String actualQuery = Brands.GetSelectQueryStatic("luobawang");

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectAllQueryStatic_happyPath() {
        final String expectedQuery = "select * from brands;";

        final String actualQuery = Brands.GetSelectAllQueryStatic();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testFromHTMLTableStr_singleEntry_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>brand_name</TH></TR>" +
                "<TR><TD>Dole</TD></TR>" +
                "</TABLE>";

        final Set<Brands> convertedObjs = Brands.FromHTMLTableStr(htmlTableStr);

        assertEquals(1, convertedObjs.size());
        assertTrue(convertedObjs.contains(new Brands("Dole")));
    }

    @Test
    public void testFromHTMLTableStr_multipleEntries_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>brand_name</TH></TR>" +
                "<TR><TD>Dole</TD></TR>" +
                "<TR><TD>default</TD></TR>" +
                "<TR><TD>Glico</TD></TR>" +
                "</TABLE>";

        final Set<Brands> convertedObjs = Brands.FromHTMLTableStr(htmlTableStr);

        assertEquals(3, convertedObjs.size());
        assertTrue(convertedObjs.contains(new Brands("Dole")));
        assertTrue(convertedObjs.contains(new Brands("default")));
        assertTrue(convertedObjs.contains(new Brands("Glico")));
    }

    @Test
    public void testFromHTMLTableStr_emptyTable_returnsEmptySet() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>brand_name</TH></TR>" +
                "</TABLE>";

        final Set<Brands> convertedObjs = Brands.FromHTMLTableStr(htmlTableStr);

        assertTrue(convertedObjs.isEmpty());
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesAllValid_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>brand_name</TH></TR>" +
                "<TR><TD>Dole</TD></TR>" +
                "<TR><TD>default</TD></TR>" +
                "</TABLE>" +
                "<TABLE BORDER=1>" +
                "<TR><TH>brand_name</TH></TR>" +
                "<TR><TD>Glico</TD></TR>" +
                "</TABLE>";

        final Set<Brands> convertedObjs = Brands.FromHTMLTableStr(htmlTableStr);

        assertEquals(3, convertedObjs.size());
        assertTrue(convertedObjs.contains(new Brands("Dole")));
        assertTrue(convertedObjs.contains(new Brands("default")));
        assertTrue(convertedObjs.contains(new Brands("Glico")));
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesOneValid_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>brand_name</TH></TR>" +
                "<TR><TD>Dole</TD></TR>" +
                "<TR><TD>default</TD></TR>" +
                "</TABLE>" +
                "<TABLE BORDER=1>" +
                "<TR><TH>brand_name</TH><TH>some_other_fields</TH></TR>" +
                "<TR><TD>Glico</TD><TD>blah1</TD></TR>" +
                "</TABLE>";

        final Set<Brands> convertedObjs = Brands.FromHTMLTableStr(htmlTableStr);

        assertEquals(2, convertedObjs.size());
        assertTrue(convertedObjs.contains(new Brands("Dole")));
        assertTrue(convertedObjs.contains(new Brands("default")));
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesNoneValid_returnsEmptyTable() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>brand_name</TH><TH>some_other_fields</TH></TR>" +
                "<TR><TD>Dole</TD><TD>blah1</TD></TR>" +
                "<TR><TD>default</TD><TD>blah2</TD></TR>" +
                "</TABLE>" +
                "<TABLE BORDER=1>" +
                "<TR><TH>brand_name</TH><TH>some_other_fields</TH></TR>" +
                "<TR><TD>Glico</TD><TD>blah3</TD></TR>" +
                "</TABLE>";

        final Set<Brands> convertedObjs = Brands.FromHTMLTableStr(htmlTableStr);

        assertTrue(convertedObjs.isEmpty());
    }

    @Test
    public void testIsValidTableSchema_schemaValid_returnsTrue() {
        final String validTableSchema = "<TABLE BORDER=1>" +
                "<TR><TH>brand_name</TH></TR>" +
                "<TR><TD>Dole</TD></TR>" +
                "<TR><TD>default</TD></TR>" +
                "<TR><TD>Nongshim</TD></TR>" +
                "</TABLE>";

        final boolean isValidSchema = Brands.isValidTableSchema(Jsoup.parse(validTableSchema));

        assertTrue(isValidSchema);
    }

    @Test
    public void testIsValidTableSchema_extraField_returnsFalse() {
        final String extraFieldTableSchema = "<TABLE BORDER=1>" +
                "<TR><TH>brand_name</TH><TH>some_other_fields</TH></TR>" +
                "<TR><TD>Dole</TD><TD>blah1</TD></TR>" +
                "<TR><TD>default</TD><TD>blah2</TD></TR>" +
                "<TR><TD>Nongshim</TD><TD>blah3</TD></TR>" +
                "</TABLE>";

        final boolean isValidSchema = Brands.isValidTableSchema(Jsoup.parse(extraFieldTableSchema));

        assertFalse(isValidSchema);
    }
}