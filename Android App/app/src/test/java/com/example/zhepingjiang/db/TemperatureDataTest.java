package com.example.zhepingjiang.db;

import org.junit.Test;

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
}
