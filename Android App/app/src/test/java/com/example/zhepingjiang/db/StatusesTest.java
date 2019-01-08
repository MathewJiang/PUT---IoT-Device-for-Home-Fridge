package com.example.zhepingjiang.db;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Statuses Table Unit Tests.
 */
public class StatusesTest {

    @Test
    public void testGetTableName_happyPath() {
        final String expectedTableName = "statuses";

        final String actualTableName = new Statuses().getTableName();

        assertEquals(expectedTableName, actualTableName);
    }

    @Test
    public void testGetInsertQuery_defaultStatus_happyPath() {
        final String expectedQuery = "insert into statuses values ('default');";

        final String actualQuery = new Statuses().getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetInsertQuery_arbitraryStatus_happyPath() {
        final String expectedQuery = "insert into statuses values ('unopened');";

        final String actualQuery = new Statuses("unopened").getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_defaultStatus_happyPath() {
        final String expectedQuery = "insert ignore into statuses values ('default');";

        final String actualQuery = new Statuses().getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_arbitraryStatus_happyPath() {
        final String expectedQuery = "insert ignore into statuses values ('unopened');";

        final String actualQuery = new Statuses("unopened").getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQuery_defaultStatus_happyPath() {
        final String expectedQuery = "delete from statuses where status = 'default';";

        final String actualQuery = new Statuses().getDeleteQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQuery_arbitraryStatus_happyPath() {
        final String expectedQuery = "delete from statuses where status = 'unopened';";

        final String actualQuery = new Statuses("unopened").getDeleteQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQuery_defaultStatus_happyPath() {
        final String expectedQuery = "select * from statuses where status = 'default';";

        final String actualQuery = new Statuses().getSelectQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQuery_arbitraryStatus_happyPath() {
        final String expectedQuery = "select * from statuses where status = 'unopened';";

        final String actualQuery = new Statuses("unopened").getSelectQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQueryStatic_happyPath() {
        final String expectedQuery = "delete from statuses where status = 'good';";

        final String actualQuery = Statuses.GetDeleteQueryStatic("good");

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQueryStatic_happyPath() {
        final String expectedQuery = "select * from statuses where status = 'good';";

        final String actualQuery = Statuses.GetSelectQueryStatic("good");

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectAllQueryStatic_happyPath() {
        final String expectedQuery = "select * from statuses;";

        final String actualQuery = Statuses.GetSelectAllQueryStatic();

        assertEquals(expectedQuery, actualQuery);
    }
}