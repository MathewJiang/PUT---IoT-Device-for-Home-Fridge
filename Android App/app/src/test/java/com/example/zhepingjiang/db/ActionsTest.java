package com.example.zhepingjiang.db;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Actions Table Unit Tests.
 */
public class ActionsTest {
    
    @Test
    public void testGetTableName_happyPath() {
        final String expectedTableName = "actions";

        final String actualTableName = new Actions().getTableName();

        assertEquals(expectedTableName, actualTableName);
    }

    @Test
    public void testGetInsertQuery_defaultAction_happyPath() {
        final String expectedQuery = "insert into actions values ('default');";

        final String actualQuery = new Actions().getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetInsertQuery_arbitraryAction_happyPath() {
        final String expectedQuery = "insert into actions values ('retain');";

        final String actualQuery = new Actions("retain").getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_defaultAction_happyPath() {
        final String expectedQuery = "insert ignore into actions values ('default');";

        final String actualQuery = new Actions().getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_arbitraryAction_happyPath() {
        final String expectedQuery = "insert ignore into actions values ('retain');";

        final String actualQuery = new Actions("retain").getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQuery_defaultAction_happyPath() {
        final String expectedQuery = "delete from actions where user_action = 'default';";

        final String actualQuery = new Actions().getDeleteQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQuery_arbitraryAction_happyPath() {
        final String expectedQuery = "delete from actions where user_action = 'retain';";

        final String actualQuery = new Actions("retain").getDeleteQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQuery_defaultAction_happyPath() {
        final String expectedQuery = "select * from actions where user_action = 'default';";

        final String actualQuery = new Actions().getSelectQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQuery_arbitraryAction_happyPath() {
        final String expectedQuery = "select * from actions where user_action = 'retain';";

        final String actualQuery = new Actions("retain").getSelectQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDelectQueryStatic_happyPath() {
        final String expectedQuery = "delete from actions where user_action = 'retain';";

        final String actualQuery = Actions.GetDeleteQueryStatic("retain");

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQueryStatic_happyPath() {
        final String expectedQuery = "select * from actions where user_action = 'retain';";

        final String actualQuery = Actions.GetSelectQueryStatic("retain");

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectAllQueryStatic_happyPath() {
        final String expectedQuery = "select * from actions;";

        final String actualQuery = Actions.GetSelectAllQueryStatic();

        assertEquals(expectedQuery, actualQuery);
    }
}