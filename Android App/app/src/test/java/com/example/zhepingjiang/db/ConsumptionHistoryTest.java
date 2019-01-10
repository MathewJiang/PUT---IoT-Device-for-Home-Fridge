package com.example.zhepingjiang.db;

import com.google.common.collect.Lists;

import org.jsoup.Jsoup;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Consumption History Table Unit Tests.
 */
public class ConsumptionHistoryTest {

    @Test
    public void testGetTableName_happyPath() {
        final String expectedTableName = "consumption_history";

        final String actualTableName = new ConsumptionHistory(6, new StdNames("Orange juice"),
                500, 500, new Actions("comsuption"), "2019-01-07 19:23:45")
                        .getTableName();

        assertEquals(expectedTableName, actualTableName);
    }

    @Test
    public void testGetPartialFieldsStr_happyPath() {
        final String expectedStr =
                "(uid, std_name, consumed_quantity, remaining_quantity, action, time_stamp)";

        final String actualStr = new ConsumptionHistory(6, new StdNames("Orange juice"),
                500, 500, new Actions("comsuption"), "2019-01-07 19:23:45")
                        .getPartialFieldsStr();

        assertEquals(expectedStr, actualStr);
    }

    @Test
    public void testBuildConsumptionHistory_happyPath() {
        final int uid = 6;
        final StdNames stdName = new StdNames("Orange juice");
        final int consumedQuantity = 800;
        final int remainingQuantity = 200;
        final Actions action = new Actions("consumption");
        final String timeStamp = "2019-01-07 19:23:45";
        final int defaultOpid = 0;

        final ConsumptionHistory allArgsObj = new ConsumptionHistory(uid, stdName, consumedQuantity,
                remainingQuantity, action, timeStamp);

        assertEquals(uid, allArgsObj.getUid());
        assertEquals(stdName.getStdName(), allArgsObj.getStdName().getStdName());
        assertEquals(consumedQuantity, allArgsObj.getConsumedQuantity());
        assertEquals(remainingQuantity, allArgsObj.getRemainingQuantity());
        assertEquals(action, allArgsObj.getAction());
        assertEquals(timeStamp, allArgsObj.getTimeStamp());
        assertEquals(defaultOpid, allArgsObj.getOpid());
    }

    @Test
    public void testGetInsertQuery_happyPath() {
        final int uid = 6;
        final StdNames stdName = new StdNames("Orange juice", new Categories("drink"));
        final int consumedQuantity = 800;
        final int remainingQuantity = 200;
        final Actions action = new Actions("consumption");
        final String timeStamp = "2019-01-07 19:23:45";
        final String expectedQuery = stdName.getUpsertQuery() +
                action.getUpsertQuery() +
                "insert into consumption_history " +
                "(uid, std_name, consumed_quantity, remaining_quantity, action, time_stamp)" +
                " values (6,'Orange juice',800,200,'consumption','2019-01-07 19:23:45');";

        final String actualQuery = new ConsumptionHistory(uid, stdName, consumedQuantity,
                remainingQuantity, action, timeStamp).getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_happyPath() {
        final int uid = 6;
        final StdNames stdName = new StdNames("Orange juice", new Categories("drink"));
        final int consumedQuantity = 800;
        final int remainingQuantity = 200;
        final Actions action = new Actions("consumption");
        final String timeStamp = "2019-01-07 19:23:45";
        final String expectedQuery = stdName.getUpsertQuery() +
                action.getUpsertQuery() +
                "insert ignore into consumption_history " +
                "(uid, std_name, consumed_quantity, remaining_quantity, action, time_stamp)" +
                " values (6,'Orange juice',800,200,'consumption','2019-01-07 19:23:45');";

        final String actualQuery = new ConsumptionHistory(uid, stdName, consumedQuantity,
                remainingQuantity, action, timeStamp).getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQuery_happyPath() {
        final String expectedQuery = "delete from consumption_history where opid = 1;";

        final ConsumptionHistory keyObj = new ConsumptionHistory(6, new StdNames("Orange juice", new Categories("drink")),
                800, 200, new Actions("consumption"), "2019-01-07 19:23:45");
        keyObj.setOpid(1);

        final String actualQuery = keyObj.getDeleteQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQuery_happyPath() {
        final String expectedQuery = "select * from consumption_history where opid = 1;";

        final ConsumptionHistory keyObj = new ConsumptionHistory(6, new StdNames("Orange juice", new Categories("drink")),
                800, 200, new Actions("consumption"), "2019-01-07 19:23:45");
        keyObj.setOpid(1);

        final String actualQuery = keyObj.getSelectQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQueryStatic_happyPath() {
        final String expectedQuery = "delete from consumption_history where opid = 6;";

        final String actualQuery = ConsumptionHistory.GetDeleteQueryStatic(6);

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQueryStatic_happyPath() {
        final String expectedQuery = "select * from consumption_history where opid = 6;";

        final String actualQuery = ConsumptionHistory.GetSelectQueryStatic(6);

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectAllQueryStatic_happyPath() {
        final String expectedQuery = "select * from consumption_history;";

        final String actualQuery = ConsumptionHistory.GetSelectAllQueryStatic();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testFromHTMLTableStr_singleEntry_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>opid</TH><TH>uid</TH><TH>std_name</TH><TH>consumed_quantity</TH><TH>remaining_quantity</TH><TH>action</TH><TH>time_stamp</TH></TR>" +
                "<TR><TD>1</TD><TD>2</TD><TD>egg</TD><TD>4</TD><TD>8</TD><TD>consumption</TD><TD>2018-12-24 09:16:32</TD></TR>" +
                "</TABLE>";

        final Set<ConsumptionHistory> convertedObjs = ConsumptionHistory.FromHTMLTableStr(htmlTableStr);

        assertEquals(1, convertedObjs.size());
        final ConsumptionHistory convertedObj = Lists.newArrayList(convertedObjs).get(0);
        assertEquals(1, convertedObj.getOpid());
        assertEquals(2, convertedObj.getUid());
        assertEquals("egg", convertedObj.getStdName().getStdName());
        assertEquals(4, convertedObj.getConsumedQuantity());
        assertEquals(8, convertedObj.getRemainingQuantity());
        assertEquals("consumption", convertedObj.getAction().getUserAction());
        assertEquals("2018-12-24 09:16:32", convertedObj.getTimeStamp());
    }

    @Test
    public void testFromHTMLTableStr_multipleEntries_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>opid</TH><TH>uid</TH><TH>std_name</TH><TH>consumed_quantity</TH><TH>remaining_quantity</TH><TH>action</TH><TH>time_stamp</TH></TR>" +
                "<TR><TD>1</TD><TD>1</TD><TD>egg</TD><TD>6</TD><TD>6</TD><TD>consumption</TD><TD>2018-12-24 09:16:32</TD></TR>" +
                "<TR><TD>3</TD><TD>2</TD><TD>egg</TD><TD>12</TD><TD>0</TD><TD>disposal</TD><TD>2018-12-26 09:16:32</TD></TR>" +
                "<TR><TD>4</TD><TD>4</TD><TD>3.25% milk</TD><TD>500</TD><TD>500</TD><TD>consumption</TD><TD>2018-12-24 09:16:32</TD></TR>" +
                "<TR><TD>6</TD><TD>5</TD><TD>oyster meat</TD><TD>500</TD><TD>250</TD><TD>consumption</TD><TD>2018-12-26 09:16:32</TD></TR>" +
                "<TR><TD>7</TD><TD>6</TD><TD>whole chicken</TD><TD>2</TD><TD>1</TD><TD>consumption</TD><TD>2018-12-27 09:16:32</TD></TR>" +
                "<TR><TD>8</TD><TD>8</TD><TD>Orange juice</TD><TD>800</TD><TD>200</TD><TD>consumption</TD><TD>2019-01-07 21:23:45</TD></TR>" +
                "</TABLE>";

        final Set<ConsumptionHistory> convertedObjs = ConsumptionHistory.FromHTMLTableStr(htmlTableStr);

        assertEquals(6, convertedObjs.size());
    }

    @Test
    public void testFromHTMLTableStr_severalRowsMissingFields_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>opid</TH><TH>uid</TH><TH>std_name</TH><TH>consumed_quantity</TH><TH>remaining_quantity</TH><TH>action</TH><TH>time_stamp</TH></TR>" +
                "<TR><TD>1</TD><TD>1</TD><TD>egg</TD><TD>6</TD><TD>6</TD><TD>consumption</TD><TD>2018-12-24 09:16:32</TD></TR>" +
                "<TR><TD>3</TD><TD>egg</TD><TD>12</TD><TD>0</TD><TD>disposal</TD><TD>2018-12-26 09:16:32</TD></TR>" +   // Invalid
                "<TR><TD>4</TD><TD>4</TD><TD>3.25% milk</TD><TD>500</TD><TD>500</TD><TD>consumption</TD><TD>2018-12-24 09:16:32</TD></TR>" +
                "<TR><TD>6</TD><TD>5</TD><TD>500</TD><TD>250</TD><TD>consumption</TD><TD>2018-12-26 09:16:32</TD></TR>" +   // Invalid
                "<TR><TD>7</TD><TD>6</TD><TD>whole chicken</TD><TD>2</TD><TD>1</TD><TD>consumption</TD><TD>2018-12-27 09:16:32</TD></TR>" +
                "<TR><TD>8</TD><TD>8</TD><TD>Orange juice</TD><TD>800</TD><TD>200</TD><TD>consumption</TD><TD>2019-01-07 21:23:45</TD></TR>" +
                "</TABLE>";

        final Set<ConsumptionHistory> convertedObjs = ConsumptionHistory.FromHTMLTableStr(htmlTableStr);

        assertEquals(4, convertedObjs.size());
    }

    @Test
    public void testFromHTMLTableStr_emptyTable_returnsEmptySet() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>opid</TH><TH>uid</TH><TH>std_name</TH><TH>consumed_quantity</TH><TH>remaining_quantity</TH><TH>action</TH><TH>time_stamp</TH></TR>" +
                "</TABLE>";

        final Set<ConsumptionHistory> convertedObjs = ConsumptionHistory.FromHTMLTableStr(htmlTableStr);

        assertTrue(convertedObjs.isEmpty());
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesAllValid_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>opid</TH><TH>uid</TH><TH>std_name</TH><TH>consumed_quantity</TH><TH>remaining_quantity</TH><TH>action</TH><TH>time_stamp</TH></TR>" +
                "<TR><TD>1</TD><TD>1</TD><TD>egg</TD><TD>6</TD><TD>6</TD><TD>consumption</TD><TD>2018-12-24 09:16:32</TD></TR>" +
                "<TR><TD>4</TD><TD>4</TD><TD>3.25% milk</TD><TD>500</TD><TD>500</TD><TD>consumption</TD><TD>2018-12-24 09:16:32</TD></TR>" +
                "</TABLE>" +
                "<TABLE BOARDER=1>" +
                "<TR><TH>opid</TH><TH>uid</TH><TH>std_name</TH><TH>consumed_quantity</TH><TH>remaining_quantity</TH><TH>action</TH><TH>time_stamp</TH></TR>" +
                "<TR><TD>7</TD><TD>6</TD><TD>whole chicken</TD><TD>2</TD><TD>1</TD><TD>consumption</TD><TD>2018-12-27 09:16:32</TD></TR>" +
                "<TR><TD>8</TD><TD>8</TD><TD>Orange juice</TD><TD>800</TD><TD>200</TD><TD>consumption</TD><TD>2019-01-07 21:23:45</TD></TR>" +
                "</TABLE>";

        final Set<ConsumptionHistory> convertedObjs = ConsumptionHistory.FromHTMLTableStr(htmlTableStr);

        assertEquals(4, convertedObjs.size());
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesOneValid_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>consumed_quantity</TH><TH>remaining_quantity</TH><TH>action</TH><TH>time_stamp</TH></TR>" +   // Invalid
                "<TR><TD>1</TD><TD>egg</TD><TD>6</TD><TD>6</TD><TD>consumption</TD><TD>2018-12-24 09:16:32</TD></TR>" +
                "<TR><TD>4</TD><TD>3.25% milk</TD><TD>500</TD><TD>500</TD><TD>consumption</TD><TD>2018-12-24 09:16:32</TD></TR>" +
                "</TABLE>" +
                "<TABLE BOARDER=1>" +
                "<TR><TH>opid</TH><TH>uid</TH><TH>std_name</TH><TH>consumed_quantity</TH><TH>remaining_quantity</TH><TH>action</TH><TH>time_stamp</TH></TR>" +
                "<TR><TD>7</TD><TD>6</TD><TD>whole chicken</TD><TD>2</TD><TD>1</TD><TD>consumption</TD><TD>2018-12-27 09:16:32</TD></TR>" +
                "<TR><TD>8</TD><TD>8</TD><TD>Orange juice</TD><TD>800</TD><TD>200</TD><TD>consumption</TD><TD>2019-01-07 21:23:45</TD></TR>" +
                "</TABLE>";

        final Set<ConsumptionHistory> convertedObjs = ConsumptionHistory.FromHTMLTableStr(htmlTableStr);

        assertEquals(2, convertedObjs.size());
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesNoneValid_returnsEmptyTable() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>consumed_quantity</TH><TH>remaining_quantity</TH><TH>action</TH><TH>time_stamp</TH></TR>" +
                "<TR><TD>1</TD><TD>egg</TD><TD>6</TD><TD>6</TD><TD>consumption</TD><TD>2018-12-24 09:16:32</TD></TR>" +
                "<TR><TD>4</TD><TD>3.25% milk</TD><TD>500</TD><TD>500</TD><TD>consumption</TD><TD>2018-12-24 09:16:32</TD></TR>" +
                "</TABLE>" +
                "<TABLE BOARDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>consumed_quantity</TH><TH>remaining_quantity</TH><TH>action</TH><TH>time_stamp</TH></TR>" +
                "<TR><TD>7</TD><TD>6</TD><TD>whole chicken</TD><TD>2</TD><TD>1</TD><TD>consumption</TD><TD>2018-12-27 09:16:32</TD></TR>" +
                "<TR><TD>8</TD><TD>8</TD><TD>Orange juice</TD><TD>800</TD><TD>200</TD><TD>consumption</TD><TD>2019-01-07 21:23:45</TD></TR>" +
                "</TABLE>";

        final Set<ConsumptionHistory> convertedObjs = ConsumptionHistory.FromHTMLTableStr(htmlTableStr);

        assertTrue(convertedObjs.isEmpty());
    }


    @Test
    public void testIsValidTableSchema_schemaValid_returnsTrue() {
        final String validTableSchema = "<TABLE BORDER=1>" +
                "<TR><TH>opid</TH><TH>uid</TH><TH>std_name</TH><TH>consumed_quantity</TH><TH>remaining_quantity</TH><TH>action</TH><TH>time_stamp</TH></TR>" +
                "<TR><TD>1</TD><TD>1</TD><TD>egg</TD><TD>6</TD><TD>6</TD><TD>consumption</TD><TD>2018-12-24 09:16:32</TD></TR>" +
                "<TR><TD>3</TD><TD>2</TD><TD>egg</TD><TD>12</TD><TD>0</TD><TD>disposal</TD><TD>2018-12-26 09:16:32</TD></TR>" +
                "<TR><TD>4</TD><TD>4</TD><TD>3.25% milk</TD><TD>500</TD><TD>500</TD><TD>consumption</TD><TD>2018-12-24 09:16:32</TD></TR>" +
                "<TR><TD>6</TD><TD>5</TD><TD>oyster meat</TD><TD>500</TD><TD>250</TD><TD>consumption</TD><TD>2018-12-26 09:16:32</TD></TR>" +
                "<TR><TD>7</TD><TD>6</TD><TD>whole chicken</TD><TD>2</TD><TD>1</TD><TD>consumption</TD><TD>2018-12-27 09:16:32</TD></TR>" +
                "<TR><TD>8</TD><TD>8</TD><TD>Orange juice</TD><TD>800</TD><TD>200</TD><TD>consumption</TD><TD>2019-01-07 21:23:45</TD></TR>" +
                "</TABLE>";

        final boolean isValidSchema = ConsumptionHistory.isValidTableSchema(Jsoup.parse(validTableSchema));

        assertTrue(isValidSchema);
    }

    @Test
    public void testIsValidTableSchema_missingFields_returnsFalse() {
        final String missingFieldsTableSchema = "<TABLE BORDER=1>" +
                "<TR><TH>opid</TH><TH>std_name</TH><TH>consumed_quantity</TH><TH>remaining_quantity</TH><TH>action</TH><TH>time_stamp</TH></TR>" +
                "<TR><TD>1</TD><TD>egg</TD><TD>6</TD><TD>6</TD><TD>consumption</TD><TD>2018-12-24 09:16:32</TD></TR>" +
                "<TR><TD>3</TD><TD>egg</TD><TD>12</TD><TD>0</TD><TD>disposal</TD><TD>2018-12-26 09:16:32</TD></TR>" +
                "<TR><TD>4</TD><TD>3.25% milk</TD><TD>500</TD><TD>500</TD><TD>consumption</TD><TD>2018-12-24 09:16:32</TD></TR>" +
                "<TR><TD>6</TD><TD>oyster meat</TD><TD>500</TD><TD>250</TD><TD>consumption</TD><TD>2018-12-26 09:16:32</TD></TR>" +
                "<TR><TD>7</TD><TD>whole chicken</TD><TD>2</TD><TD>1</TD><TD>consumption</TD><TD>2018-12-27 09:16:32</TD></TR>" +
                "<TR><TD>8</TD><TD>Orange juice</TD><TD>800</TD><TD>200</TD><TD>consumption</TD><TD>2019-01-07 21:23:45</TD></TR>" +
                "</TABLE>";

        final boolean isValidSchema = ConsumptionHistory.isValidTableSchema(Jsoup.parse(missingFieldsTableSchema));

        assertFalse(isValidSchema);
    }
}