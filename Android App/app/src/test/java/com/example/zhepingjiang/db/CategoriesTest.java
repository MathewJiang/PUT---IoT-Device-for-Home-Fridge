package com.example.zhepingjiang.db;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Categories Table Unit Tests.
 */
public class CategoriesTest {

    @Test
    public void testGetTableName_happyPath() {
        final String expectedTableName = "categories";

        final String actualTableName = new Categories().getTableName();

        assertEquals(expectedTableName, actualTableName);
    }

    @Test
    public void testGetInsertQuery_defaultCategory_happyPath() {
        final String expectedQuery = "insert into categories values ('uncategorized');";

        final String actualQuery = new Categories().getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetInsertQuery_arbitraryCategory_happyPath() {
        final String expectedQuery = "insert into categories values ('canned food');";

        final String actualQuery = new Categories("canned food").getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_defaultCategory_happyPath() {
        final String expectedQuery = "insert ignore into categories values ('uncategorized');";

        final String actualQuery = new Categories().getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_arbitraryCategory_happyPath() {
        final String expectedQuery = "insert ignore into categories values ('canned food');";

        final String actualQuery = new Categories("canned food").getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQuery_defaultCategory_happyPath() {
        final String expectedQuery = "delete from categories where category = 'uncategorized';";

        final String actualQuery = new Categories().getDeleteQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQuery_arbitraryCategory_happyPath() {
        final String expectedQuery = "delete from categories where category = 'canned food';";

        final String actualQuery = new Categories("canned food").getDeleteQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQuery_defaultCategory_happyPath() {
        final String expectedQuery = "select * from categories where category = 'uncategorized';";

        final String actualQuery = new Categories().getSelectQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQuery_arbitraryCategory_happyPath() {
        final String expectedQuery = "select * from categories where category = 'canned food';";

        final String actualQuery = new Categories("canned food").getSelectQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQueryStatic_happyPath() {
        final String expectedQuery = "delete from categories where category = 'drink';";

        final String actualQuery = Categories.GetDeleteQueryStatic("drink");

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQueryStatic_happyPath() {
        final String expectedQuery = "select * from categories where category = 'drink';";

        final String actualQuery = Categories.GetSelectQueryStatic("drink");

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectAllQueryStatic_happyPath() {
        final String expectedQuery = "select * from categories;";

        final String actualQuery = Categories.GetSelectAllQueryStatic();

        assertEquals(expectedQuery, actualQuery);
    }
}