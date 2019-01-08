package com.example.zhepingjiang.db;

import org.junit.Test;

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
}