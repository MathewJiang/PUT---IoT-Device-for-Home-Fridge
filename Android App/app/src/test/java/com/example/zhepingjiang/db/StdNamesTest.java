package com.example.zhepingjiang.db;

import com.google.common.collect.Lists;

import org.jsoup.Jsoup;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Std Names Table Unit Tests.
 */
public class StdNamesTest {

    @Test
    public void testGetTableName_happyPath() {
        final String expectedTableName = "std_names";

        final String actualTableName = new StdNames("somename").getTableName();

        assertEquals(expectedTableName, actualTableName);
    }

    @Test
    public void testBuildStdNames_defaultConstr_withDefaultCategory() {
        final Categories expectedCategory = new Categories();

        final StdNames defaultObj = new StdNames("somename");

        assertEquals(expectedCategory.getCategory(), defaultObj.getCategory().getCategory());
    }

    @Test
    public void testBuildStdNames_allArgsConstr_withSpecifiedCategory() {
        final Categories expectedCategory = new Categories("someCategory");

        final StdNames allArgsObj = new StdNames("somename", expectedCategory);

        assertEquals(expectedCategory.getCategory(), allArgsObj.getCategory().getCategory());
    }

    @Test
    public void testGetInsertQuery_uncategorizedItem_happyPath() {
        final Categories expectedCategory = new Categories();
        final String expectedQuery = expectedCategory.getUpsertQuery() +
                "insert into std_names values ('Coco-cola','uncategorized');";

        final String actualQuery = new StdNames("Coco-cola").getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetInsertQuery_categorizedItem_happyPath() {
        final Categories expectedCategory = new Categories("drink");
        final String expectedQuery = expectedCategory.getUpsertQuery() +
                "insert into std_names values ('Coco-cola','drink');";

        final String actualQuery = new StdNames("Coco-cola", expectedCategory).getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_uncategorizedItem_happyPath() {
        final Categories expectedCategory = new Categories();
        final String expectedQuery = expectedCategory.getUpsertQuery() +
                "insert ignore into std_names values ('Coco-cola','uncategorized');";

        final String actualQuery = new StdNames("Coco-cola").getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_categorizedItem_happyPath() {
        final Categories expectedCategory = new Categories("drink");
        final String expectedQuery = expectedCategory.getUpsertQuery() +
                "insert ignore into std_names values ('Coco-cola','drink');";

        final String actualQuery = new StdNames("Coco-cola", expectedCategory).getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQuery_happyPath() {
        final String expectedQuery = "delete from std_names where std_name = 'Coco-cola';";

        final String actualQuery = new StdNames("Coco-cola").getDeleteQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQuery_happyPath() {
        final String expectedQuery = "select * from std_names where std_name = 'Coco-cola';";

        final String actualQuery = new StdNames("Coco-cola").getSelectQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQueryStatic_happyPath() {
        final String expectedQuery = "delete from std_names where std_name = 'Orange juice';";

        final String actualQuery = StdNames.GetDeleteQueryStatic("Orange juice");

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQueryStatic_happyPath() {
        final String expectedQuery = "select * from std_names where std_name = 'Orange juice';";

        final String actualQuery = StdNames.GetSelectQueryStatic("Orange juice");

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectAllQueryStatic_happyPath() {
        final String expectedQuery = "select * from std_names;";

        final String actualQuery = StdNames.GetSelectAllQueryStatic();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testFromHTMLTableStr_singleEntry_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>std_name</TH><TH>category</TH></TR>" +
                "<TR><TD>orange juice</TD><TD>juice</TD></TR>" +
                "</TABLE>";

        final Set<StdNames> convertedObjs = StdNames.FromHTMLTableStr(htmlTableStr);

        assertEquals(1, convertedObjs.size());
        final StdNames convertedObj = Lists.newArrayList(convertedObjs).get(0);
        assertEquals("orange juice", convertedObj.getStdName());
        assertEquals("juice", convertedObj.getCategory().getCategory());
    }

    @Test
    public void testFromHTMLTableStr_multipleEntries_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>std_name</TH><TH>category</TH></TR>" +
                "<TR><TD>orange juice</TD><TD>juice</TD></TR>" +
                "<TR><TD>apple</TD><TD>fruit</TD></TR>" +
                "<TR><TD>oxtail</TD><TD>meat</TD></TR>" +
                "<TR><TD>banana</TD><TD>fruit</TD></TR>" +
                "<TR><TD>Coco-cola</TD><TD>drink</TD></TR>" +
                "<TR><TD>chocolate</TD><TD>snack</TD></TR>" +
                "</TABLE>";

        final Set<StdNames> convertedObjs = StdNames.FromHTMLTableStr(htmlTableStr);

        assertEquals(6, convertedObjs.size());
    }

    @Test
    public void testFromHTMLTableStr_emptyTable_returnsEmptySet() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>std_name</TH><TH>category</TH></TR>" +
                "</TABLE>";

        final Set<StdNames> convertedObjs = StdNames.FromHTMLTableStr(htmlTableStr);

        assertTrue(convertedObjs.isEmpty());
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesAllValid_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>std_name</TH><TH>category</TH></TR>" +
                "<TR><TD>orange juice</TD><TD>juice</TD></TR>" +
                "<TR><TD>apple</TD><TD>fruit</TD></TR>" +
                "<TR><TD>oxtail</TD><TD>meat</TD></TR>" +
                "</TABLE>" +
                "<TABLE BOARDER=1>" +
                "<TR><TH>std_name</TH><TH>category</TH></TR>" +
                "<TR><TD>banana</TD><TD>fruit</TD></TR>" +
                "<TR><TD>Coco-cola</TD><TD>drink</TD></TR>" +
                "<TR><TD>chocolate</TD><TD>snack</TD></TR>" +
                "</TABLE>";

        final Set<StdNames> convertedObjs = StdNames.FromHTMLTableStr(htmlTableStr);

        assertEquals(6, convertedObjs.size());
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesOneValid_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>std_name</TH><TH>category</TH></TR>" +
                "<TR><TD>orange juice</TD><TD>juice</TD></TR>" +
                "<TR><TD>apple</TD><TD>fruit</TD></TR>" +
                "<TR><TD>oxtail</TD><TD>meat</TD></TR>" +
                "</TABLE>" +
                "<TABLE BOARDER=1>" +
                "<TR><TH>std_name</TH><TH>category</TH><TH>other_field</TH></TR>" +
                "<TR><TD>banana</TD><TD>fruit</TD><TD>blah1</TD></TR>" +
                "<TR><TD>Coco-cola</TD><TD>drink</TD><TD>blah2</TD></TR>" +
                "<TR><TD>chocolate</TD><TD>snack</TD><TD>blah3</TD></TR>" +
                "</TABLE>";

        final Set<StdNames> convertedObjs = StdNames.FromHTMLTableStr(htmlTableStr);

        assertEquals(3, convertedObjs.size());
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesNoneValid_returnsEmptyTable() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>std_name</TH><TH>category</TH><TH>other_field</TH></TR>" +
                "<TR><TD>orange juice</TD><TD>blah1</TD><TD>juice</TD></TR>" +
                "<TR><TD>apple</TD><TD>blah2</TD><TD>fruit</TD></TR>" +
                "<TR><TD>oxtail</TD><TD>blah3</TD><TD>meat</TD></TR>" +
                "</TABLE>" +
                "<TABLE BOARDER=1>" +
                "<TR><TH>std_name</TH><TH>category</TH><TH>other_field</TH></TR>" +
                "<TR><TD>banana</TD><TD>fruit</TD><TD>blah1</TD></TR>" +
                "<TR><TD>Coco-cola</TD><TD>drink</TD><TD>blah2</TD></TR>" +
                "<TR><TD>chocolate</TD><TD>snack</TD><TD>blah3</TD></TR>" +
                "</TABLE>";

        final Set<StdNames> convertedObjs = StdNames.FromHTMLTableStr(htmlTableStr);

        assertTrue(convertedObjs.isEmpty());
    }


    @Test
    public void testIsValidTableSchema_schemaValid_returnsTrue() {
        final String validTableSchema = "<TABLE BORDER=1>" +
                "<TR><TH>std_name</TH><TH>category</TH></TR>" +
                "<TR><TD>orange juice</TD><TD>juice</TD></TR>" +
                "<TR><TD>apple</TD><TD>fruit</TD></TR>" +
                "<TR><TD>oxtail</TD><TD>meat</TD></TR>" +
                "<TR><TD>banana</TD><TD>fruit</TD></TR>" +
                "<TR><TD>Coco-cola</TD><TD>drink</TD></TR>" +
                "<TR><TD>chocolate</TD><TD>snack</TD></TR>" +
                "</TABLE>";

        final boolean isValidSchema = StdNames.isValidTableSchema(Jsoup.parse(validTableSchema));

        assertTrue(isValidSchema);
    }

    @Test
    public void testIsValidTableSchema_extraFields_returnsFalse() {
        final String extraFieldTableSchema = "<TABLE BORDER=1>" +
                "<TR><TH>std_name</TH><TH>category</TH><TH>other_field</TH></TR>" +
                "<TR><TD>orange juice</TD><TD>blah1</TD><TD>juice</TD></TR>" +
                "<TR><TD>apple</TD><TD>blah2</TD><TD>fruit</TD></TR>" +
                "<TR><TD>oxtail</TD><TD>blah3</TD><TD>meat</TD></TR>" +
                "</TABLE>";

        final boolean isValidSchema = StdNames.isValidTableSchema(Jsoup.parse(extraFieldTableSchema));

        assertFalse(isValidSchema);
    }
}