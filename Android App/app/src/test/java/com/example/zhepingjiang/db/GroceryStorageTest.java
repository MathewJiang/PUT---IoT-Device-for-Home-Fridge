package com.example.zhepingjiang.db;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Grocery Storage Table Unit Tests.
 */
public class GroceryStorageTest {

    @Test
    public void testGetTableName_happyPath() {
        final String expectedTableName = "grocery_storage";

        final String actualTableName = new GroceryStorage(6, new StdNames("Orange juice"), 500,
                new ContentUnits("ml"), "2019-01-07 19:23:45", "2019-01-03", "2019-01-12", new Statuses("good"))
                        .getTableName();

        assertEquals(expectedTableName, actualTableName);
    }

    @Test
    public void testBuildGroceryStorage_allArgsConstr_happyPath() {
        final int uid = 6;
        final StdNames stdName = new StdNames("Orange juice", new Categories("drink"));
        final int contentQuantity = 800;
        final ContentUnits contentUnit = new ContentUnits("ml");
        final String lastUpdatedTimeStamp = "2019-01-07 19:23:45";
        final String purchaseDate = "2019-01-03";
        final String expiryDate = "2019-01-12";
        final Statuses status = new Statuses("good");

        final GroceryStorage allArgsObj = new GroceryStorage(uid, stdName, contentQuantity, contentUnit,
                lastUpdatedTimeStamp, purchaseDate, expiryDate, status);

        assertEquals(uid, allArgsObj.getUid());
        assertEquals(stdName.getStdName(), allArgsObj.getStdName().getStdName());
        assertEquals(contentQuantity, allArgsObj.getContentQuantity());
        assertEquals(contentUnit, allArgsObj.getContentUnit());
        assertEquals(lastUpdatedTimeStamp, allArgsObj.getLastUpdatedTimeStamp());
        assertEquals(purchaseDate, allArgsObj.getPurchaseDate());
        assertEquals(expiryDate, allArgsObj.getExpiryDate());
        assertEquals(status, allArgsObj.getStatus());
    }

    @Test
    public void testBuildGroceryStorage_fromPurchaseHistory_happyPath() {
        final StdNames stdName = new StdNames("Orange juice", new Categories("drink"));
        final Vendors vendor = new Vendors("Loblaws");
        final Brands brand = new Brands("Dole");
        final int contentQuantity = 1000;
        final char isPackaged = 'T';
        final ContentUnits contentUnit = new ContentUnits("ml");
        final PackageUnits packageUnit = new PackageUnits("box");
        final String purchaseDate = "2019-01-03";
        final String expiryDate = "2019-02-03";
        final int uid = 6;
        final String lastUpdatedTimeStamp = "2019-01-07 19:23:45";
        final Statuses status = new Statuses("unopened");

        final PurchaseHistory ph = new PurchaseHistory(uid, stdName, vendor, brand, contentQuantity,
                contentUnit, isPackaged, packageUnit, purchaseDate, expiryDate);
        final GroceryStorage groceryStorage = new GroceryStorage(ph, status, lastUpdatedTimeStamp);

        assertEquals(uid, groceryStorage.getUid());
        assertEquals(stdName.getStdName(), groceryStorage.getStdName().getStdName());
        assertEquals(contentQuantity, groceryStorage.getContentQuantity());
        assertEquals(contentUnit, groceryStorage.getContentUnit());
        assertEquals(lastUpdatedTimeStamp, groceryStorage.getLastUpdatedTimeStamp());
        assertEquals(purchaseDate, groceryStorage.getPurchaseDate());
        assertEquals(expiryDate, groceryStorage.getExpiryDate());
        assertEquals(status, groceryStorage.getStatus());
    }

    @Test
    public void testGetInsertQuery_happyPath() {
        final int uid = 6;
        final StdNames stdName = new StdNames("Orange juice", new Categories("drink"));
        final int contentQuantity = 800;
        final ContentUnits contentUnit = new ContentUnits("ml");
        final String lastUpdatedTimeStamp = "2019-01-07 19:23:45";
        final String purchaseDate = "2019-01-03";
        final String expiryDate = "2019-01-12";
        final Statuses status = new Statuses("good");
        final String expectedQuery = stdName.getUpsertQuery() +
                contentUnit.getUpsertQuery() +
                status.getUpsertQuery() +
                "insert into grocery_storage values " +
                "(6,'Orange juice',800,'ml','2019-01-07 19:23:45','2019-01-03','2019-01-12','good');";

        final String actualQuery = new GroceryStorage(uid, stdName, contentQuantity, contentUnit,
                lastUpdatedTimeStamp, purchaseDate, expiryDate, status).getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_happyPath() {
        final int uid = 6;
        final StdNames stdName = new StdNames("Orange juice", new Categories("drink"));
        final int contentQuantity = 800;
        final ContentUnits contentUnit = new ContentUnits("ml");
        final String lastUpdatedTimeStamp = "2019-01-07 19:23:45";
        final String purchaseDate = "2019-01-03";
        final String expiryDate = "2019-01-12";
        final Statuses status = new Statuses("good");
        final String expectedQuery = stdName.getUpsertQuery() +
                contentUnit.getUpsertQuery() +
                status.getUpsertQuery() +
                "insert ignore into grocery_storage values " +
                "(6,'Orange juice',800,'ml','2019-01-07 19:23:45','2019-01-03','2019-01-12','good');";

        final String actualQuery = new GroceryStorage(uid, stdName, contentQuantity, contentUnit,
                lastUpdatedTimeStamp, purchaseDate, expiryDate, status).getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQuery_happyPath() {
        final String expectedQuery = "delete from grocery_storage where uid = 6;";

        final String actualQuery = new GroceryStorage(6, new StdNames("Orange juice"), 500,
                new ContentUnits("ml"), "2019-01-07 19:23:45", "2019-01-03", "2019-01-12", new Statuses("good"))
                        .getDeleteQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQuery_happyPath() {
        final String expectedQuery = "select * from grocery_storage where uid = 6;";

        final String actualQuery = new GroceryStorage(6, new StdNames("Orange juice"), 500,
                new ContentUnits("ml"), "2019-01-07 19:23:45", "2019-01-03", "2019-01-12", new Statuses("good"))
                        .getSelectQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQueryStatic_happyPath() {
        final String expectedQuery = "delete from grocery_storage where uid = 6;";

        final String actualQuery = GroceryStorage.GetDeleteQueryStatic(6);

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQueryStatic_happyPath() {
        final String expectedQuery = "select * from grocery_storage where uid = 6;";

        final String actualQuery = GroceryStorage.GetSelectQueryStatic(6);

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectAllQueryStatic_happyPath() {
        final String expectedQuery = "select * from grocery_storage;";

        final String actualQuery = GroceryStorage.GetSelectAllQueryStatic();

        assertEquals(expectedQuery, actualQuery);
    }
}