package com.example.zhepingjiang.db;

import org.junit.Test;

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
}
