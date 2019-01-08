package com.example.zhepingjiang.db;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Vendors Table Unit Tests.
 */
public class VendorsTest {

    @Test
    public void testGetTableName_happyPath() {
        final String expectedTableName = "vendors";

        final String actualTableName = new Vendors().getTableName();

        assertEquals(expectedTableName, actualTableName);
    }

    @Test
    public void testGetInsertQuery_defaultVendor_happyPath() {
        final String expectedQuery = "insert into vendors values ('default');";

        final String actualQuery = new Vendors().getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetInsertQuery_arbitraryVendor_happyPath() {
        final String expectedQuery = "insert into vendors values ('Walmart');";

        final String actualQuery = new Vendors("Walmart").getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_defaultVendor_happyPath() {
        final String expectedQuery = "insert ignore into vendors values ('default');";

        final String actualQuery = new Vendors().getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_arbitraryVendor_happyPath() {
        final String expectedQuery = "insert ignore into vendors values ('Walmart');";

        final String actualQuery = new Vendors("Walmart").getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQuery_defaultVendor_happyPath() {
        final String expectedQuery = "delete from vendors where vendor_name = 'default';";

        final String actualQuery = new Vendors().getDeleteQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQuery_arbitraryVendor_happyPath() {
        final String expectedQuery = "delete from vendors where vendor_name = 'Walmart';";

        final String actualQuery = new Vendors("Walmart").getDeleteQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQuery_defaultVendor_happyPath() {
        final String expectedQuery = "select * from vendors where vendor_name = 'default';";

        final String actualQuery = new Vendors().getSelectQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQuery_arbitraryVendor_happyPath() {
        final String expectedQuery = "select * from vendors where vendor_name = 'Walmart';";

        final String actualQuery = new Vendors("Walmart").getSelectQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQueryStatic_happyPath() {
        final String expectedQuery = "delete from vendors where vendor_name = 'Loblaws';";

        final String actualQuery = Vendors.GetDeleteQueryStatic("Loblaws");

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQueryStatic_happyPath() {
        final String expectedQuery = "select * from vendors where vendor_name = 'Loblaws';";

        final String actualQuery = Vendors.GetSelectQueryStatic("Loblaws");

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectAllQueryStatic_happyPath() {
        final String expectedQuery = "select * from vendors;";

        final String actualQuery = Vendors.GetSelectAllQueryStatic();

        assertEquals(expectedQuery, actualQuery);
    }
}