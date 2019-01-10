package com.example.zhepingjiang.db;

import com.google.common.collect.Lists;

import org.jsoup.Jsoup;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Temperature Data Table Unit Tests
 */
public class TemperatureDataTest {

    @Test
    public void testGetTableName_happyPath() {
        final String expectedTableName = "temperature_data";

        final String actualTableName = new TemperatureData("2019-01-01 19:20:21", -3.5).getTableName();

        assertEquals(expectedTableName, actualTableName);
    }

    @Test
    public void testGetInsertQuery_happyPath() {
        final String expectedQuery = "insert into temperature_data (time, temperature) values ('2019-01-01 19:20:21',-3.5);";

        final String actualQuery = new TemperatureData("2019-01-01 19:20:21", -3.5).getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_happyPath() {
        final String expectedQuery = "insert ignore into temperature_data (time, temperature) values ('2019-01-01 19:20:21',-3.5);";

        final String actualQuery = new TemperatureData("2019-01-01 19:20:21", -3.5).getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQuery_happyPath() {
        final String expectedQuery = "delete from temperature_data where rid = 6;";

        final TemperatureData keyObj = new TemperatureData("2019-01-01 19:20:21", -3.5);
        keyObj.setRid(6);

        final String actualQuery = keyObj.getDeleteQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQuery_happyPath() {
        final String expectedQuery = "select * from temperature_data where rid = 6;";

        final TemperatureData keyObj = new TemperatureData("2019-01-01 19:20:21", -3.5);
        keyObj.setRid(6);

        final String actualQuery = keyObj.getSelectQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQueryStatic_happyPath() {
        final String expectedQuery = "delete from temperature_data where rid = 6;";

        final String actualQuery = TemperatureData.GetDeleteQueryStatic(6);

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQueryStatic_happyPath() {
        final String expectedQuery = "select * from temperature_data where rid = 6;";

        final String actualQuery = TemperatureData.GetSelectQueryStatic(6);

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectAllQueryStatic_happyPath() {
        final String expectedQuery = "select * from temperature_data;";

        final String actualQuery = TemperatureData.GetSelectAllQueryStatic();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testFromHTMLTableStr_singleEntry_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>rid</TH><TH>time</TH><TH>temperature</TH></TR>" +
                "<TR><TD>1</TD><TD>2019-01-03 15:37:29</TD><TD>-2.5</TD></TR>" +
                "</TABLE>";

        final Set<TemperatureData> convertedObjs = TemperatureData.FromHTMLTableStr(htmlTableStr);

        assertEquals(1, convertedObjs.size());
        final TemperatureData convertedObj = Lists.newArrayList(convertedObjs).get(0);
        assertEquals(1, convertedObj.getRid());
        assertEquals("2019-01-03 15:37:29", convertedObj.getTime());
        assertEquals(-2.5, convertedObj.getTemperature(), 0.01);
    }

    @Test
    public void testFromHTMLTableStr_multipleEntries_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>rid</TH><TH>time</TH><TH>temperature</TH></TR>" +
                "<TR><TD>1</TD><TD>2019-01-03 15:37:29</TD><TD>-2.5</TD></TR>" +
                "<TR><TD>2</TD><TD>2019-01-04 03:57:54</TD><TD>-3.7</TD></TR>" +
                "<TR><TD>3</TD><TD>2019-01-05 08:12:38</TD><TD>-5.1</TD></TR>" +
                "<TR><TD>4</TD><TD>2019-01-06 04:45:15</TD><TD>-4.6</TD></TR>" +
                "<TR><TD>5</TD><TD>2019-01-07 03:16:28</TD><TD>-3.91</TD></TR>" +
                "<TR><TD>6</TD><TD>2019-01-08 19:27:21</TD><TD>-1.9</TD></TR>" +
                "</TABLE>";

        final Set<TemperatureData> convertedObjs = TemperatureData.FromHTMLTableStr(htmlTableStr);

        assertEquals(6, convertedObjs.size());
    }

    @Test
    public void testFromHTMLTableStr_emptyTable_returnsEmptySet() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>rid</TH><TH>time</TH><TH>temperature</TH></TR>" +
                "</TABLE>";

        final Set<TemperatureData> convertedObjs = TemperatureData.FromHTMLTableStr(htmlTableStr);

        assertTrue(convertedObjs.isEmpty());
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesAllValid_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>rid</TH><TH>time</TH><TH>temperature</TH></TR>" +
                "<TR><TD>1</TD><TD>2019-01-03 15:37:29</TD><TD>-2.5</TD></TR>" +
                "<TR><TD>2</TD><TD>2019-01-04 03:57:54</TD><TD>-3.7</TD></TR>" +
                "<TR><TD>3</TD><TD>2019-01-05 08:12:38</TD><TD>-5.1</TD></TR>" +
                "</TABLE>" +
                "<TABLE BOARDER=1>" +
                "<TR><TH>rid</TH><TH>time</TH><TH>temperature</TH></TR>" +
                "<TR><TD>4</TD><TD>2019-01-06 04:45:15</TD><TD>-4.6</TD></TR>" +
                "<TR><TD>5</TD><TD>2019-01-07 03:16:28</TD><TD>-3.91</TD></TR>" +
                "<TR><TD>6</TD><TD>2019-01-08 19:27:21</TD><TD>-1.9</TD></TR>" +
                "</TABLE>";

        final Set<TemperatureData> convertedObjs = TemperatureData.FromHTMLTableStr(htmlTableStr);

        assertEquals(6, convertedObjs.size());
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesOneValid_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>rid</TH><TH>time</TH><TH>temperature</TH></TR>" +
                "<TR><TD>1</TD><TD>2019-01-03 15:37:29</TD><TD>-2.5</TD></TR>" +
                "<TR><TD>2</TD><TD>2019-01-04 03:57:54</TD><TD>-3.7</TD></TR>" +
                "<TR><TD>3</TD><TD>2019-01-05 08:12:38</TD><TD>-5.1</TD></TR>" +
                "</TABLE>" +
                "<TABLE BOARDER=1>" +
                "<TR><TH>rid</TH><TH>time</TH><TH>temperature</TH><TH>other_fields</TH></TR>" +
                "<TR><TD>4</TD><TD>2019-01-06 04:45:15</TD><TD>-4.6</TD><TD>blah1</TD></TR>" +
                "<TR><TD>5</TD><TD>2019-01-07 03:16:28</TD><TD>-3.91</TD><TD>blah2</TD></TR>" +
                "<TR><TD>6</TD><TD>2019-01-08 19:27:21</TD><TD>-1.9</TD><TD>blah3</TD></TR>" +
                "</TABLE>";

        final Set<TemperatureData> convertedObjs = TemperatureData.FromHTMLTableStr(htmlTableStr);

        assertEquals(3, convertedObjs.size());
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesNoneValid_returnsEmptyTable() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>rid</TH><TH>other_fields</TH><TH>time</TH><TH>temperature</TH></TR>" +
                "<TR><TD>1</TD><TD>blah1</TD><TD>2019-01-03 15:37:29</TD><TD>-2.5</TD></TR>" +
                "<TR><TD>2</TD><TD>blah2</TD><TD>2019-01-04 03:57:54</TD><TD>-3.7</TD></TR>" +
                "<TR><TD>3</TD><TD>blah3</TD><TD>2019-01-05 08:12:38</TD><TD>-5.1</TD></TR>" +
                "</TABLE>" +
                "<TABLE BOARDER=1>" +
                "<TR><TH>rid</TH><TH>time</TH><TH>temperature</TH><TH>other_fields</TH></TR>" +
                "<TR><TD>4</TD><TD>2019-01-06 04:45:15</TD><TD>-4.6/TD><TD>blah4</TD></TR>" +
                "<TR><TD>5</TD><TD>2019-01-07 03:16:28</TD><TD>-3.91/TD><TD>blah5</TD></TR>" +
                "<TR><TD>6</TD><TD>2019-01-08 19:27:21</TD><TD>-1.9/TD><TD>blah6</TD></TR>" +
                "</TABLE>";

        final Set<TemperatureData> convertedObjs = TemperatureData.FromHTMLTableStr(htmlTableStr);

        assertTrue(convertedObjs.isEmpty());
    }


    @Test
    public void testIsValidTableSchema_schemaValid_returnsTrue() {
        final String validTableSchema = "<TABLE BORDER=1>" +
                "<TR><TH>rid</TH><TH>time</TH><TH>temperature</TH></TR>" +
                "<TR><TD>1</TD><TD>2019-01-03 15:37:29</TD><TD>-2.5</TD></TR>" +
                "<TR><TD>2</TD><TD>2019-01-04 03:57:54</TD><TD>-3.7</TD></TR>" +
                "<TR><TD>3</TD><TD>2019-01-05 08:12:38</TD><TD>-5.1</TD></TR>" +
                "</TABLE>";

        final boolean isValidSchema = TemperatureData.isValidTableSchema(Jsoup.parse(validTableSchema));

        assertTrue(isValidSchema);
    }

    @Test
    public void testIsValidTableSchema_extraFields_returnsFalse() {
        final String extraFieldTableSchema = "<TABLE BOARDER=1>" +
                "<TR><TH>rid</TH><TH>time</TH><TH>temperature</TH><TH>other_fields</TH></TR>" +
                "<TR><TD>4</TD><TD>2019-01-06 04:45:15</TD><TD>-4.6</TD><TD>blah4</TD></TR>" +
                "<TR><TD>5</TD><TD>2019-01-07 03:16:28</TD><TD>-3.91</TD><TD>blah5</TD></TR>" +
                "<TR><TD>6</TD><TD>2019-01-08 19:27:21</TD><TD>-1.9</TD><TD>blah6</TD></TR>" +
                "</TABLE>";

        final boolean isValidSchema = TemperatureData.isValidTableSchema(Jsoup.parse(extraFieldTableSchema));

        assertFalse(isValidSchema);
    }
}
