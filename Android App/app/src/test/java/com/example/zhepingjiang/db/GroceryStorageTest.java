package com.example.zhepingjiang.db;

import com.google.common.collect.Lists;

import org.jsoup.Jsoup;
import org.junit.Test;

import java.util.Set;

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
        final boolean isPackaged = true;
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
    public void testGetInsertQuery_UIDUnspecified_replaceWithLastInsertIDQuery() {
        final int uid = 0;
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
                "(LAST_INSERT_ID(),'Orange juice',800,'ml','2019-01-07 19:23:45','2019-01-03','2019-01-12','good');";

        final String actualQuery = new GroceryStorage(0, stdName, contentQuantity, contentUnit,
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
    public void testGetUpsertQuery_UIDUnspecified_replaceWithLastInsertIDQuery() {
        final int uid = 0;
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
                "(LAST_INSERT_ID(),'Orange juice',800,'ml','2019-01-07 19:23:45','2019-01-03','2019-01-12','good');";

        final String actualQuery = new GroceryStorage(0, stdName, contentQuantity, contentUnit,
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

    @Test
    public void testFromHTMLTableStr_singleEntry_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>content_quantity</TH><TH>content_unit</TH><TH>last_updated</TH><TH>purchase_date</TH><TH>expiry_date</TH><TH>status</TH></TR>" +
                "<TR><TD>3</TD><TD>3.25% milk</TD><TD>1000</TD><TD>ml</TD><TD>2018-12-21 00:00:01</TD><TD>2018-12-10</TD><TD>2018-12-20</TD><TD>expired</TD></TR>" +
                "</TABLE>";

        final Set<GroceryStorage> convertedObjs = GroceryStorage.FromHTMLTableStr(htmlTableStr);

        assertEquals(1, convertedObjs.size());
        final GroceryStorage convertedObj = Lists.newArrayList(convertedObjs).get(0);
        assertEquals(3, convertedObj.getUid());
        assertEquals("3.25% milk", convertedObj.getStdName().getStdName());
        assertEquals(1000, convertedObj.getContentQuantity());
        assertEquals("ml", convertedObj.getContentUnit().getUnit());;
        assertEquals("2018-12-21 00:00:01", convertedObj.getLastUpdatedTimeStamp());
        assertEquals("2018-12-10", convertedObj.getPurchaseDate());
        assertEquals("2018-12-20", convertedObj.getExpiryDate());
        assertEquals("expired", convertedObj.getStatus().getStatus());
    }

    @Test
    public void testFromHTMLTableStr_multipleEntries_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>content_quantity</TH><TH>content_unit</TH><TH>last_updated</TH><TH>purchase_date</TH><TH>expiry_date</TH><TH>status</TH></TR>" +
                "<TR><TD>3</TD><TD>3.25% milk</TD><TD>1000</TD><TD>ml</TD><TD>2018-12-10 06:16:32</TD><TD>2018-12-10</TD><TD>2018-12-20</TD><TD>expired</TD></TR>" +
                "<TR><TD>5</TD><TD>oyster meat</TD><TD>250</TD><TD>g</TD><TD>2018-12-26 09:16:32</TD><TD>2018-12-26</TD><TD>2018-12-28</TD><TD>good</TD></TR>" +
                "<TR><TD>6</TD><TD>whole chicken</TD><TD>1</TD><TD>lb</TD><TD>2018-12-27 09:16:32</TD><TD>2018-12-21</TD><TD>2018-12-31</TD><TD>good</TD></TR>" +
                "<TR><TD>8</TD><TD>Orange juice</TD><TD>1000</TD><TD>ml</TD><TD>2019-01-07 19:43:52</TD><TD>2019-01-07</TD><TD>2019-02-03</TD><TD>unopened</TD></TR>" +
                "</TABLE>";

        final Set<GroceryStorage> convertedObjs = GroceryStorage.FromHTMLTableStr(htmlTableStr);

        assertEquals(4, convertedObjs.size());
    }

    @Test
    public void testFromHTMLTableStr_severalRowsMissingFields_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>content_quantity</TH><TH>content_unit</TH><TH>last_updated</TH><TH>purchase_date</TH><TH>expiry_date</TH><TH>status</TH></TR>" +
                "<TR><TD>3.25% milk</TD><TD>1000</TD><TD>ml</TD><TD>2018-12-10 06:16:32</TD><TD>2018-12-10</TD><TD>2018-12-20</TD><TD>expired</TD></TR>" + // Invalid
                "<TR><TD>5</TD><TD>250</TD><TD>g</TD><TD>2018-12-26 09:16:32</TD><TD>2018-12-26</TD><TD>2018-12-28</TD><TD>good</TD></TR>" + // Invalid
                "<TR><TD>6</TD><TD>whole chicken</TD><TD>1</TD><TD>2018-12-27 09:16:32</TD><TD>2018-12-21</TD><TD>2018-12-31</TD><TD>good</TD></TR>" + // Invalid
                "<TR><TD>8</TD><TD>Orange juice</TD><TD>1000</TD><TD>ml</TD><TD>2019-01-07 19:43:52</TD><TD>2019-01-07</TD><TD>2019-02-03</TD><TD>unopened</TD></TR>" +
                "</TABLE>";

        final Set<GroceryStorage> convertedObjs = GroceryStorage.FromHTMLTableStr(htmlTableStr);

        assertEquals(1, convertedObjs.size());
    }

    @Test
    public void testFromHTMLTableStr_emptyTable_returnsEmptySet() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>content_quantity</TH><TH>content_unit</TH><TH>last_updated</TH><TH>purchase_date</TH><TH>expiry_date</TH><TH>status</TH></TR>" +
                "</TABLE>";
        final Set<GroceryStorage> convertedObjs = GroceryStorage.FromHTMLTableStr(htmlTableStr);

        assertTrue(convertedObjs.isEmpty());
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesAllValid_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>content_quantity</TH><TH>content_unit</TH><TH>last_updated</TH><TH>purchase_date</TH><TH>expiry_date</TH><TH>status</TH></TR>" +
                "<TR><TD>3</TD><TD>3.25% milk</TD><TD>1000</TD><TD>ml</TD><TD>2018-12-10 06:16:32</TD><TD>2018-12-10</TD><TD>2018-12-20</TD><TD>expired</TD></TR>" +
                "<TR><TD>5</TD><TD>oyster meat</TD><TD>250</TD><TD>g</TD><TD>2018-12-26 09:16:32</TD><TD>2018-12-26</TD><TD>2018-12-28</TD><TD>good</TD></TR>" +
                "</TABLE>" +
                "<TABLE BOARDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>content_quantity</TH><TH>content_unit</TH><TH>last_updated</TH><TH>purchase_date</TH><TH>expiry_date</TH><TH>status</TH></TR>" +
                "<TR><TD>6</TD><TD>whole chicken</TD><TD>1</TD><TD>lb</TD><TD>2018-12-27 09:16:32</TD><TD>2018-12-21</TD><TD>2018-12-31</TD><TD>good</TD></TR>" +
                "<TR><TD>8</TD><TD>Orange juice</TD><TD>1000</TD><TD>ml</TD><TD>2019-01-07 19:43:52</TD><TD>2019-01-07</TD><TD>2019-02-03</TD><TD>unopened</TD></TR>" +
                "</TABLE>";

        final Set<GroceryStorage> convertedObjs = GroceryStorage.FromHTMLTableStr(htmlTableStr);

        assertEquals(4, convertedObjs.size());
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesOneValid_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>std_name</TH><TH>content_quantity</TH><TH>content_unit</TH><TH>last_updated</TH><TH>purchase_date</TH><TH>expiry_date</TH><TH>status</TH></TR>" +
                "<TR><TD>3.25% milk</TD><TD>1000</TD><TD>ml</TD><TD>2018-12-10 06:16:32</TD><TD>2018-12-10</TD><TD>2018-12-20</TD><TD>expired</TD></TR>" +
                "<TR><TD>oyster meat</TD><TD>250</TD><TD>g</TD><TD>2018-12-26 09:16:32</TD><TD>2018-12-26</TD><TD>2018-12-28</TD><TD>good</TD></TR>" +
                "</TABLE>" +
                "<TABLE BOARDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>content_quantity</TH><TH>content_unit</TH><TH>last_updated</TH><TH>purchase_date</TH><TH>expiry_date</TH><TH>status</TH></TR>" +
                "<TR><TD>6</TD><TD>whole chicken</TD><TD>1</TD><TD>lb</TD><TD>2018-12-27 09:16:32</TD><TD>2018-12-21</TD><TD>2018-12-31</TD><TD>good</TD></TR>" +
                "<TR><TD>8</TD><TD>Orange juice</TD><TD>1000</TD><TD>ml</TD><TD>2019-01-07 19:43:52</TD><TD>2019-01-07</TD><TD>2019-02-03</TD><TD>unopened</TD></TR>" +
                "</TABLE>";

        final Set<GroceryStorage> convertedObjs = GroceryStorage.FromHTMLTableStr(htmlTableStr);

        assertEquals(2, convertedObjs.size());
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesNoneValid_returnsEmptyTable() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>std_name</TH><TH>vendor</TH><TH>content_quantity</TH><TH>content_unit</TH><TH>is_packaged</TH><TH>package_unit</TH><TH>purchase_date</TH><TH>expiry_date</TH></TR>" +
                "<TR><TD>1</TD><TD>egg</TD><TD>Metro</TD><TD>Prestige</TD><TD>12</TD><TD>items</TD><TD>T</TD><TD>box</TD><TD>2018-12-21</TD><TD>2018-12-31</TD></TR>" +
                "<TR><TD>2</TD><TD>egg</TD><TD>Metro</TD><TD>Prestige</TD><TD>12</TD><TD>items</TD><TD>T</TD><TD>box</TD><TD>2018-12-21</TD><TD>2018-12-28</TD></TR>" +
                "<TR><TD>3</TD><TD>3.25% milk</TD><TD>Metro</TD><TD>Sealtest</TD><TD>1000</TD><TD>ml</TD><TD>T</TD><TD>box</TD><TD>2018-12-10</TD><TD>2018-12-20</TD></TR>" +
                "</TABLE>" +
                "<TABLE BOARDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>brand</TH><TH>content_quantity</TH><TH>is_packaged</TH><TH>package_unit</TH><TH>purchase_date</TH><TH>expiry_date</TH></TR>" +
                "<TR><TD>5</TD><TD>oyster meat</TD><TD>Metro</TD><TD>Fanny Bay</TD><TD>500</TD><TD>g</TD><TD>T</TD><TD>box</TD><TD>2018-12-26</TD><TD>2018-12-28</TD></TR>" +
                "<TR><TD>6</TD><TD>whole chicken</TD><TD>Lucky Moose</TD><TD>Lucky Moose</TD><TD>3</TD><TD>lb</TD><TD>T</TD><TD>box</TD><TD>2018-12-21</TD><TD>2018-12-31</TD></TR>" +
                "<TR><TD>8</TD><TD>Orange juice</TD><TD>Loblaws</TD><TD>Dole</TD><TD>1000</TD><TD>ml</TD><TD>T</TD><TD>box</TD><TD>2019-01-07</TD><TD>2019-02-03</TD></TR>" +
                "</TABLE>";

        final Set<GroceryStorage> convertedObjs = GroceryStorage.FromHTMLTableStr(htmlTableStr);

        assertTrue(convertedObjs.isEmpty());
    }


    @Test
    public void testIsValidTableSchema_schemaValid_returnsTrue() {
        final String validTableSchema = "<TABLE BORDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>content_quantity</TH><TH>content_unit</TH><TH>last_updated</TH><TH>purchase_date</TH><TH>expiry_date</TH><TH>status</TH></TR>" +
                "<TR><TD>3</TD><TD>3.25% milk</TD><TD>1000</TD><TD>ml</TD><TD>2018-12-10 06:16:32</TD><TD>2018-12-10</TD><TD>2018-12-20</TD><TD>expired</TD></TR>" +
                "<TR><TD>5</TD><TD>oyster meat</TD><TD>250</TD><TD>g</TD><TD>2018-12-26 09:16:32</TD><TD>2018-12-26</TD><TD>2018-12-28</TD><TD>good</TD></TR>" +
                "<TR><TD>6</TD><TD>whole chicken</TD><TD>1</TD><TD>lb</TD><TD>2018-12-27 09:16:32</TD><TD>2018-12-21</TD><TD>2018-12-31</TD><TD>good</TD></TR>" +
                "<TR><TD>8</TD><TD>Orange juice</TD><TD>1000</TD><TD>ml</TD><TD>2019-01-07 19:43:52</TD><TD>2019-01-07</TD><TD>2019-02-03</TD><TD>unopened</TD></TR>" +
                "</TABLE>";

        final boolean isValidSchema = GroceryStorage.isValidTableSchema(Jsoup.parse(validTableSchema));

        assertTrue(isValidSchema);
    }

    @Test
    public void testIsValidTableSchema_missingFields_returnsFalse() {
        final String missingFieldsTableSchema = "<TABLE BORDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>content_unit</TH><TH>last_updated</TH><TH>purchase_date</TH><TH>expiry_date</TH><TH>status</TH></TR>" +
                "<TR><TD>3</TD><TD>3.25% milk</TD><TD>ml</TD><TD>2018-12-10 06:16:32</TD><TD>2018-12-10</TD><TD>2018-12-20</TD><TD>expired</TD></TR>" +
                "<TR><TD>5</TD><TD>oyster meat</TD><TD>g</TD><TD>2018-12-26 09:16:32</TD><TD>2018-12-26</TD><TD>2018-12-28</TD><TD>good</TD></TR>" +
                "<TR><TD>6</TD><TD>whole chicken</TD><TD>lb</TD><TD>2018-12-27 09:16:32</TD><TD>2018-12-21</TD><TD>2018-12-31</TD><TD>good</TD></TR>" +
                "<TR><TD>8</TD><TD>Orange juice</TD><TD>ml</TD><TD>2019-01-07 19:43:52</TD><TD>2019-01-07</TD><TD>2019-02-03</TD><TD>unopened</TD></TR>" +
                "</TABLE>";

        final boolean isValidSchema = GroceryStorage.isValidTableSchema(Jsoup.parse(missingFieldsTableSchema));

        assertFalse(isValidSchema);
    }
}