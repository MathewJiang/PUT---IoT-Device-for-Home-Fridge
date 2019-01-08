package com.example.zhepingjiang.db;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Brands Table Unit Tests.
 */
public class BrandsTest {

    @Test
    public void testGetTableName_happyPath() {
        final String expectedTableName = "brands";

        final String actualTableName = new Brands().getTableName();

        assertEquals(expectedTableName, actualTableName);
    }

    @Test
    public void testGetInsertQuery_defaultBrand_happyPath() {
        final String expectedQuery = "insert into brands values ('default');";

        final String actualQuery = new Brands().getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetInsertQuery_arbitraryBrand_happyPath() {
        final String expectedQuery = "insert into brands values ('luobawang');";

        final String actualQuery = new Brands("luobawang").getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_defaultBrand_happyPath() {
        final String expectedQuery = "insert ignore into brands values ('default');";

        final String actualQuery = new Brands().getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_arbitraryBrand_happyPath() {
        final String expectedQuery = "insert ignore into brands values ('luobawang');";

        final String actualQuery = new Brands("luobawang").getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQuery_defaultBrand_happyPath() {
        final String expectedQuery = "delete from brands where brand_name = 'default';";

        final String actualQuery = new Brands().getDeleteQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQuery_arbitraryBrand_happyPath() {
        final String expectedQuery = "delete from brands where brand_name = 'luobawang';";

        final String actualQuery = new Brands("luobawang").getDeleteQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQuery_defaultBrand_happyPath() {
        final String expectedQuery = "select * from brands where brand_name = 'default';";

        final String actualQuery = new Brands().getSelectQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQuery_arbitraryBrand_happyPath() {
        final String expectedQuery = "select * from brands where brand_name = 'luobawang';";

        final String actualQuery = new Brands("luobawang").getSelectQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQueryStatic_happyPath() {
        final String expectedQuery = "delete from brands where brand_name = 'luobawang';";

        final String actualQuery = Brands.GetDeleteQueryStatic("luobawang");

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQueryStatic_happyPath() {
        final String expectedQuery = "select * from brands where brand_name = 'luobawang';";

        final String actualQuery = Brands.GetSelectQueryStatic("luobawang");

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectAllQueryStatic_happyPath() {
        final String expectedQuery = "select * from brands;";

        final String actualQuery = Brands.GetSelectAllQueryStatic();

        assertEquals(expectedQuery, actualQuery);
    }
}