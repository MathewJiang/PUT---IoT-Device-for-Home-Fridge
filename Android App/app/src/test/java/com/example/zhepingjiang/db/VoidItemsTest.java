package com.example.zhepingjiang.db;

import com.google.common.collect.Lists;

import org.jsoup.Jsoup;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Void Items Table Unit Tests.
 */
public class VoidItemsTest {

    @Test
    public void testGetTableName_happyPath() {
        final String expectedTableName = "void_items";

        final String actualTableName = new VoidItems(0, new StdNames("somename"), new Statuses(), "").getTableName();

        assertEquals(expectedTableName, actualTableName);
    }

    @Test
    public void testGetInsertQuery_happyPath() {
        final int uid = 6;
        final StdNames stdName = new StdNames("Orange juice", new Categories("drink"));
        final Statuses status = new Statuses("disposed");
        final String timeStamp = "2019-01-07 12:25:31";
        final String expectedQuery = stdName.getUpsertQuery() +
                status.getUpsertQuery() +
                "insert into void_items values (6,'Orange juice','disposed','2019-01-07 12:25:31');";

        final String actualQuery = new VoidItems(uid, stdName, status, timeStamp).getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_happyPath() {
        final int uid = 6;
        final StdNames stdName = new StdNames("Orange juice", new Categories("drink"));
        final Statuses status = new Statuses("disposed");
        final String timeStamp = "2019-01-07 12:25:31";
        final String expectedQuery = stdName.getUpsertQuery() +
                status.getUpsertQuery() +
                "insert ignore into void_items values (6,'Orange juice','disposed','2019-01-07 12:25:31');";

        final String actualQuery = new VoidItems(uid, stdName, status, timeStamp).getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQuery_happyPath() {
        final String expectedQuery = "delete from void_items where uid = 6;";

        final String actualQuery = new VoidItems(6, new StdNames("somename"), new Statuses(), "").getDeleteQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQuery_happyPath() {
        final String expectedQuery = "select * from void_items where uid = 6;";

        final String actualQuery = new VoidItems(6, new StdNames("somename"), new Statuses(), "").getSelectQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQueryStatic_happyPath() {
        final String expectedQuery = "delete from void_items where uid = 6;";

        final String actualQuery = VoidItems.GetDeleteQueryStatic(6);

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQueryStatic_happyPath() {
        final String expectedQuery = "select * from void_items where uid = 6;";

        final String actualQuery = VoidItems.GetSelectQueryStatic(6);

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectAllQueryStatic_happyPath() {
        final String expectedQuery = "select * from void_items;";

        final String actualQuery = VoidItems.GetSelectAllQueryStatic();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testFromHTMLTableStr_singleEntry_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>void_status</TH><TH>time_stamp</TH></TR>" +
                "<TR><TD>1</TD><TD>egg</TD><TD>finished</TD><TD>2018-12-25 09:16:32</TD></TR>" +
                "</TABLE>";

        final Set<VoidItems> convertedObjs = VoidItems.FromHTMLTableStr(htmlTableStr);

        assertEquals(1, convertedObjs.size());
        final VoidItems convertedObj = Lists.newArrayList(convertedObjs).get(0);
        assertEquals(1, convertedObj.getUid());
        assertEquals("egg", convertedObj.getStdName().getStdName());
        assertEquals("finished", convertedObj.getVoidStatus().getStatus());
        assertEquals("2018-12-25 09:16:32", convertedObj.getTimeStamp());

    }

    @Test
    public void testFromHTMLTableStr_multipleEntries_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>void_status</TH><TH>time_stamp</TH></TR>" +
                "<TR><TD>1</TD><TD>egg</TD><TD>finished</TD><TD>2018-12-25 09:16:32</TR>" +
                "<TR><TD>2</TD><TD>3.25% milk</TD><TD>disposed</TD><TD>2018-12-26 09:16:32</TD></TR>" +
                "<TR><TD>4</TD><TD>3.25% milk</TD><TD>finished</TD><TD>2018-12-25 09:16:32</TD></TR>" +
                "<TR><TD>5</TD><TD>tomatoes</TD><TD>finished</TD><TD>2018-12-27 09:16:32</TD></TR>" +
                "<TR><TD>7</TD><TD>cheese</TD><TD>finished</TD><TD>2018-12-28 09:16:32</TD></TR>" +
                "<TR><TD>8</TD><TD>whipping cream</TD><TD>disposed</TD><TD>2019-01-05 09:16:32</TD></TR>" +
                "</TABLE>";

        final Set<VoidItems> convertedObjs = VoidItems.FromHTMLTableStr(htmlTableStr);

        assertEquals(6, convertedObjs.size());
    }

    @Test
    public void testFromHTMLTableStr_severalRowsExtraFields_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>void_status</TH><TH>time_stamp</TH></TR>" +
                "<TR><TD>1</TD><TD>egg</TD><TD>finished</TD><TD>2018-12-25 09:16:32</TD><TD>blah1</TD></TR>" + // Invalid
                "<TR><TD>2</TD><TD>3.25% milk</TD><TD>disposed</TD><TD>2018-12-26 09:16:32</TD></TR>" +
                "<TR><TD>4</TD><TD>3.25% milk</TD><TD>finished</TD><TD>2018-12-25 09:16:32</TD></TR>" +
                "<TR><TD>5</TD><TD>tomatoes</TD><TD>finished</TD><TD>2018-12-27 09:16:32</TD><TD>blah4</TD></TR>" + // Invalid
                "<TR><TD>7</TD><TD>cheese</TD><TD>finished</TD><TD>2018-12-28 09:16:32</TD></TR>" +
                "<TR><TD>8</TD><TD>whipping cream</TD><TD>disposed</TD><TD>2019-01-05 09:16:32</TD></TR>" +
                "</TABLE>";

        final Set<VoidItems> convertedObjs = VoidItems.FromHTMLTableStr(htmlTableStr);

        assertEquals(4, convertedObjs.size());
    }

    @Test
    public void testFromHTMLTableStr_emptyTable_returnsEmptySet() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>void_status</TH><TH>time_stamp</TH></TR>" +
                "</TABLE>";

        final Set<VoidItems> convertedObjs = VoidItems.FromHTMLTableStr(htmlTableStr);

        assertTrue(convertedObjs.isEmpty());
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesAllValid_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>void_status</TH><TH>time_stamp</TH></TR>" +
                "<TR><TD>1</TD><TD>egg</TD><TD>finished</TD><TD>2018-12-25 09:16:32</TR>" +
                "<TR><TD>2</TD><TD>3.25% milk</TD><TD>disposed</TD><TD>2018-12-26 09:16:32</TD></TR>" +
                "<TR><TD>4</TD><TD>3.25% milk</TD><TD>finished</TD><TD>2018-12-25 09:16:32</TD></TR>" +
                "</TABLE>" +
                "<TABLE BOARDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>void_status</TH><TH>time_stamp</TH></TR>" +
                "<TR><TD>5</TD><TD>tomatoes</TD><TD>finished</TD><TD>2018-12-27 09:16:32</TD></TR>" +
                "<TR><TD>7</TD><TD>cheese</TD><TD>finished</TD><TD>2018-12-28 09:16:32</TD></TR>" +
                "<TR><TD>8</TD><TD>whipping cream</TD><TD>disposed</TD><TD>2019-01-05 09:16:32</TD></TR>" +
                "</TABLE>";

        final Set<VoidItems> convertedObjs = VoidItems.FromHTMLTableStr(htmlTableStr);

        assertEquals(6, convertedObjs.size());
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesOneValid_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>void_status</TH><TH>time_stamp</TH><TH>extra_field</TH></TR>" +
                "<TR><TD>1</TD><TD>egg</TD><TD>finished</TD><TD>2018-12-25 09:16:32</TR>" +
                "<TR><TD>2</TD><TD>3.25% milk</TD><TD>disposed</TD><TD>2018-12-26 09:16:32</TD></TR>" +
                "<TR><TD>4</TD><TD>3.25% milk</TD><TD>finished</TD><TD>2018-12-25 09:16:32</TD></TR>" +
                "</TABLE>" +
                "<TABLE BOARDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>void_status</TH><TH>time_stamp</TH></TR>" +
                "<TR><TD>5</TD><TD>tomatoes</TD><TD>finished</TD><TD>2018-12-27 09:16:32</TD></TR>" +
                "<TR><TD>7</TD><TD>cheese</TD><TD>finished</TD><TD>2018-12-28 09:16:32</TD></TR>" +
                "<TR><TD>8</TD><TD>whipping cream</TD><TD>disposed</TD><TD>2019-01-05 09:16:32</TD></TR>" +
                "</TABLE>";

        final Set<VoidItems> convertedObjs = VoidItems.FromHTMLTableStr(htmlTableStr);

        assertEquals(3, convertedObjs.size());
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesNoneValid_returnsEmptyTable() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>void_status</TH><TH>time_stamp</TH><TH>extra_field</TH></TR>" +
                "<TR><TD>1</TD><TD>egg</TD><TD>finished</TD><TD>2018-12-25 09:16:32</TR>" +
                "<TR><TD>2</TD><TD>3.25% milk</TD><TD>disposed</TD><TD>2018-12-26 09:16:32</TD></TR>" +
                "<TR><TD>4</TD><TD>3.25% milk</TD><TD>finished</TD><TD>2018-12-25 09:16:32</TD></TR>" +
                "</TABLE>" +
                "<TABLE BOARDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>void_status</TH><TH>time_stamp</TH><TH>extra_field</TH></TR>" +
                "<TR><TD>5</TD><TD>tomatoes</TD><TD>finished</TD><TD>2018-12-27 09:16:32</TD></TR>" +
                "<TR><TD>7</TD><TD>cheese</TD><TD>finished</TD><TD>2018-12-28 09:16:32</TD></TR>" +
                "<TR><TD>8</TD><TD>whipping cream</TD><TD>disposed</TD><TD>2019-01-05 09:16:32</TD></TR>" +
                "</TABLE>";

        final Set<VoidItems> convertedObjs = VoidItems.FromHTMLTableStr(htmlTableStr);

        assertTrue(convertedObjs.isEmpty());
    }


    @Test
    public void testIsValidTableSchema_schemaValid_returnsTrue() {
        final String validTableSchema = "<TABLE BORDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>void_status</TH><TH>time_stamp</TH></TR>" +
                "<TR><TD>1</TD><TD>egg</TD><TD>finished</TD><TD>2018-12-25 09:16:32</TD></TR>" +
                "<TR><TD>2</TD><TD>3.25% milk</TD><TD>disposed</TD><TD>2018-12-26 09:16:32</TD></TR>" +
                "<TR><TD>4</TD><TD>3.25% milk</TD><TD>finished</TD><TD>2018-12-25 09:16:32</TD></TR>" +
                "</TABLE>";

        final boolean isValidSchema = VoidItems.isValidTableSchema(Jsoup.parse(validTableSchema));

        assertTrue(isValidSchema);
    }

    @Test
    public void testIsValidTableSchema_extraField_returnsFalse() {
        final String extraFieldTableSchema = "<TABLE BORDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>void_status</TH><TH>time_stamp</TH><TH>some_field</TH></TR>" +
                "<TR><TD>1</TD><TD>egg</TD><TD>finished</TD><TD>2018-12-25 09:16:32</TD><TD>blah1</TD></TR>" +
                "<TR><TD>2</TD><TD>3.25% milk</TD><TD>disposed</TD><TD>2018-12-26 09:16:32</TD><TD>blah2</TD></TR>" +
                "<TR><TD>4</TD><TD>3.25% milk</TD><TD>finished</TD><TD>2018-12-25 09:16:32</TD><TD>blah3</TD></TR>" +
                "</TABLE>";

        final boolean isValidSchema = VoidItems.isValidTableSchema(Jsoup.parse(extraFieldTableSchema));

        assertFalse(isValidSchema);
    }
}
