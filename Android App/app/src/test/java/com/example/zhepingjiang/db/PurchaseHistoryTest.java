package com.example.zhepingjiang.db;

import com.google.common.collect.Lists;

import org.jsoup.Jsoup;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Purchase History Table Unit Tests.
 */
public class PurchaseHistoryTest {

    @Test
    public void testGetTableName_happyPath() {
        final String expectedTableName = "purchase_history";

        final String actualTableName = new PurchaseHistory(new StdNames("Orange juice")).getTableName();

        assertEquals(expectedTableName, actualTableName);
    }

    @Test
    public void testGetPartialFieldsStr_happyPath() {
        final String expectedStr =
                "(std_name, vendor, brand, content_quantity, content_unit, is_packaged, package_unit, purchase_date, expiry_date)";

        final String actualStr = new PurchaseHistory(new StdNames("Orange juice")).getPartialFieldsStr();

        assertEquals(expectedStr, actualStr);
    }

    @Test
    public void testBuildPurchaseHistory_partialConstr_withDefaultFields() {
        final StdNames stdName = new StdNames("Orange juice");
        final Vendors defaultVendor = new Vendors();
        final Brands defaultBrand = new Brands();
        final int defaultContentQuantity = 0;
        final boolean defaultIsPackaged = false;
        final ContentUnits defaultContentUnit = new ContentUnits();
        final PackageUnits defaultPackageUnit = new PackageUnits();
        final String defaultPurchaseDate = "2019-01-01";
        final String defaultExpiryDate = "2019-01-02";
        final int defaultUid = 0;

        final PurchaseHistory defaultObj = new PurchaseHistory(stdName);

        assertEquals(stdName.getStdName(), defaultObj.getStdName().getStdName());
        assertEquals(defaultVendor, defaultObj.getVendor());
        assertEquals(defaultBrand, defaultObj.getBrand());
        assertEquals(defaultContentQuantity, defaultObj.getContentQuantity());
        assertEquals(defaultIsPackaged, defaultObj.isPackaged());
        assertEquals(defaultContentUnit, defaultObj.getContentUnit());
        assertEquals(defaultPackageUnit, defaultObj.getPackageUnit());
        assertEquals(defaultPurchaseDate, defaultObj.getPurchaseDate());
        assertEquals(defaultExpiryDate, defaultObj.getExpiryDate());
        assertEquals(defaultUid, defaultObj.getUid());
    }

    @Test
    public void testBuildPurchaseHistory_allArgsConstrButUid_withSpecifiedFields() {
        final StdNames stdName = new StdNames("Orange juice");
        final Vendors vendor = new Vendors("Loblaws");
        final Brands brand = new Brands("Dole");
        final int contentQuantity = 1000;
        final boolean isPackaged = true;
        final ContentUnits contentUnit = new ContentUnits("ml");
        final PackageUnits packageUnit = new PackageUnits("box");
        final String purchaseDate = "2019-01-07";
        final String expiryDate = "2019-02-03";
        final int defaultUid = 0;

        final PurchaseHistory allArgsObj = new PurchaseHistory(stdName, vendor, brand, contentQuantity,
                contentUnit, isPackaged, packageUnit, purchaseDate, expiryDate);

        assertEquals(stdName.getStdName(), allArgsObj.getStdName().getStdName());
        assertEquals(vendor, allArgsObj.getVendor());
        assertEquals(brand, allArgsObj.getBrand());
        assertEquals(contentQuantity, allArgsObj.getContentQuantity());
        assertEquals(isPackaged, allArgsObj.isPackaged());
        assertEquals(contentUnit, allArgsObj.getContentUnit());
        assertEquals(packageUnit, allArgsObj.getPackageUnit());
        assertEquals(purchaseDate, allArgsObj.getPurchaseDate());
        assertEquals(expiryDate, allArgsObj.getExpiryDate());
        assertEquals(defaultUid, allArgsObj.getUid());
    }

    @Test
    public void testGetInsertQuery_withDefaultFields_happyPath() {
        final StdNames stdName = new StdNames("Orange juice");
        final Vendors defaultVendor = new Vendors();
        final Brands defaultBrand = new Brands();
        final int defaultContentQuantity = 0;
        final boolean defaultIsPackaged = false;
        final ContentUnits defaultContentUnit = new ContentUnits();
        final PackageUnits defaultPackageUnit = new PackageUnits();
        final String defaultPurchaseDate = "2019-01-01";
        final String defaultExpiryDate = "2019-01-02";
        final String expectedQuery = stdName.getUpsertQuery() +
                defaultVendor.getUpsertQuery() +
                defaultBrand.getUpsertQuery() +
                defaultContentUnit.getUpsertQuery() +
                defaultPackageUnit.getUpsertQuery() +
                "insert into purchase_history " +
                "(std_name, vendor, brand, content_quantity, content_unit, is_packaged, package_unit, purchase_date, expiry_date)" +
                " values ('Orange juice','default','default',0,'default','F','default','2019-01-01','2019-01-02');";

        final String actualQuery = new PurchaseHistory(stdName).getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetInsertQuery_allFieldsSpecified_happyPath() {
        final StdNames stdName = new StdNames("Orange juice");
        final Vendors vendor = new Vendors("Loblaws");
        final Brands brand = new Brands("Dole");
        final int contentQuantity = 1000;
        final boolean isPackaged = true;
        final ContentUnits contentUnit = new ContentUnits("ml");
        final PackageUnits packageUnit = new PackageUnits("box");
        final String purchaseDate = "2019-01-07";
        final String expiryDate = "2019-02-03";
        final String expectedQuery = stdName.getUpsertQuery() +
                vendor.getUpsertQuery() +
                brand.getUpsertQuery() +
                contentUnit.getUpsertQuery() +
                packageUnit.getUpsertQuery() +
                "insert into purchase_history " +
                "(std_name, vendor, brand, content_quantity, content_unit, is_packaged, package_unit, purchase_date, expiry_date)" +
                " values ('Orange juice','Loblaws','Dole',1000,'ml','T','box','2019-01-07','2019-02-03');";

        final String actualQuery = new PurchaseHistory(stdName, vendor, brand, contentQuantity, contentUnit,
                isPackaged, packageUnit, purchaseDate, expiryDate).getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_withDefaultFields_happyPath() {
        final StdNames stdName = new StdNames("Orange juice");
        final Vendors defaultVendor = new Vendors();
        final Brands defaultBrand = new Brands();
        final int defaultContentQuantity = 0;
        final char defaultIsPackaged = 'F';
        final ContentUnits defaultContentUnit = new ContentUnits();
        final PackageUnits defaultPackageUnit = new PackageUnits();
        final String defaultPurchaseDate = "2019-01-01";
        final String defaultExpiryDate = "2019-01-02";
        final String expectedQuery = stdName.getUpsertQuery() +
                defaultVendor.getUpsertQuery() +
                defaultBrand.getUpsertQuery() +
                defaultContentUnit.getUpsertQuery() +
                defaultPackageUnit.getUpsertQuery() +
                "insert ignore into purchase_history " +
                "(std_name, vendor, brand, content_quantity, content_unit, is_packaged, package_unit, purchase_date, expiry_date)" +
                " values ('Orange juice','default','default',0,'default','F','default','2019-01-01','2019-01-02');";

        final String actualQuery = new PurchaseHistory(stdName).getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_allFieldsSpecified_happyPath() {
        final StdNames stdName = new StdNames("Orange juice");
        final Vendors vendor = new Vendors("Loblaws");
        final Brands brand = new Brands("Dole");
        final int contentQuantity = 1000;
        final boolean isPackaged = true;
        final ContentUnits contentUnit = new ContentUnits("ml");
        final PackageUnits packageUnit = new PackageUnits("box");
        final String purchaseDate = "2019-01-07";
        final String expiryDate = "2019-02-03";
        final String expectedQuery = stdName.getUpsertQuery() +
                vendor.getUpsertQuery() +
                brand.getUpsertQuery() +
                contentUnit.getUpsertQuery() +
                packageUnit.getUpsertQuery() +
                "insert ignore into purchase_history " +
                "(std_name, vendor, brand, content_quantity, content_unit, is_packaged, package_unit, purchase_date, expiry_date)" +
                " values ('Orange juice','Loblaws','Dole',1000,'ml','T','box','2019-01-07','2019-02-03');";

        final String actualQuery = new PurchaseHistory(stdName, vendor, brand, contentQuantity, contentUnit,
                isPackaged, packageUnit, purchaseDate, expiryDate).getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQuery_happyPath() {
        final String expectedQuery = "delete from purchase_history where uid = 6;";

        final PurchaseHistory keyObj = new PurchaseHistory(new StdNames("Orange juice"));
        keyObj.setUid(6);

        final String actualQuery = keyObj.getDeleteQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQuery_happyPath() {
        final String expectedQuery = "select * from purchase_history where uid = 6;";

        final PurchaseHistory keyObj = new PurchaseHistory(new StdNames("Orange juice"));
        keyObj.setUid(6);

        final String actualQuery = keyObj.getSelectQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQueryStatic_happyPath() {
        final String expectedQuery = "delete from purchase_history where uid = 6;";

        final String actualQuery = PurchaseHistory.GetDeleteQueryStatic(6);

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQueryStatic_happyPath() {
        final String expectedQuery = "select * from purchase_history where uid = 6;";

        final String actualQuery = PurchaseHistory.GetSelectQueryStatic(6);

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectAllQueryStatic_happyPath() {
        final String expectedQuery = "select * from purchase_history;";

        final String actualQuery = PurchaseHistory.GetSelectAllQueryStatic();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testFromHTMLTableStr_singleEntry_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>vendor</TH><TH>brand</TH><TH>content_quantity</TH><TH>content_unit</TH><TH>is_packaged</TH><TH>package_unit</TH><TH>purchase_date</TH><TH>expiry_date</TH></TR>" +
                "<TR><TD>1</TD><TD>egg</TD><TD>Metro</TD><TD>Prestige</TD><TD>12</TD><TD>items</TD><TD>T</TD><TD>box</TD><TD>2018-12-21</TD><TD>2018-12-31</TD></TR>" +
                "</TABLE>";

        final Set<PurchaseHistory> convertedObjs = PurchaseHistory.FromHTMLTableStr(htmlTableStr);

        assertEquals(1, convertedObjs.size());
        final PurchaseHistory convertedObj = Lists.newArrayList(convertedObjs).get(0);
        assertEquals(1, convertedObj.getUid());
        assertEquals("egg", convertedObj.getStdName().getStdName());
        assertEquals("Metro", convertedObj.getVendor().getVendorName());
        assertEquals("Prestige", convertedObj.getBrand().getBrandName());
        assertEquals(12, convertedObj.getContentQuantity());
        assertEquals("items", convertedObj.getContentUnit().getUnit());
        assertEquals(true, convertedObj.isPackaged());
        assertEquals("box", convertedObj.getPackageUnit().getUnit());
        assertEquals("2018-12-21", convertedObj.getPurchaseDate());
        assertEquals("2018-12-31", convertedObj.getExpiryDate());

    }

    @Test
    public void testFromHTMLTableStr_multipleEntries_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>vendor</TH><TH>brand</TH><TH>content_quantity</TH><TH>content_unit</TH><TH>is_packaged</TH><TH>package_unit</TH><TH>purchase_date</TH><TH>expiry_date</TH></TR>" +
                "<TR><TD>1</TD><TD>egg</TD><TD>Metro</TD><TD>Prestige</TD><TD>12</TD><TD>items</TD><TD>T</TD><TD>box</TD><TD>2018-12-21</TD><TD>2018-12-31</TD></TR>" +
                "<TR><TD>2</TD><TD>egg</TD><TD>Metro</TD><TD>Prestige</TD><TD>12</TD><TD>items</TD><TD>T</TD><TD>box</TD><TD>2018-12-21</TD><TD>2018-12-28</TD></TR>" +
                "<TR><TD>3</TD><TD>3.25% milk</TD><TD>Metro</TD><TD>Sealtest</TD><TD>1000</TD><TD>ml</TD><TD>T</TD><TD>box</TD><TD>2018-12-10</TD><TD>2018-12-20</TD></TR>" +
                "<TR><TD>5</TD><TD>oyster meat</TD><TD>Metro</TD><TD>Fanny Bay</TD><TD>500</TD><TD>g</TD><TD>T</TD><TD>box</TD><TD>2018-12-26</TD><TD>2018-12-28</TD></TR>" +
                "<TR><TD>6</TD><TD>whole chicken</TD><TD>Lucky Moose</TD><TD>Lucky Moose</TD><TD>3</TD><TD>lb</TD><TD>T</TD><TD>box</TD><TD>2018-12-21</TD><TD>2018-12-31</TD></TR>" +
                "<TR><TD>8</TD><TD>Orange juice</TD><TD>Loblaws</TD><TD>Dole</TD><TD>1000</TD><TD>ml</TD><TD>T</TD><TD>box</TD><TD>2019-01-07</TD><TD>2019-02-03</TD></TR>" +
                "</TABLE>";

        final Set<PurchaseHistory> convertedObjs = PurchaseHistory.FromHTMLTableStr(htmlTableStr);

        assertEquals(6, convertedObjs.size());
    }

    @Test
    public void testFromHTMLTableStr_severalRowsMissingFields_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>vendor</TH><TH>brand</TH><TH>content_quantity</TH><TH>content_unit</TH><TH>is_packaged</TH><TH>package_unit</TH><TH>purchase_date</TH><TH>expiry_date</TH></TR>" +
                "<TR><TD>1</TD><TD>egg</TD><TD>Metro</TD><TD>Prestige</TD><TD>12</TD><TD>items</TD><TD>T</TD><TD>box</TD><TD>2018-12-21</TD><TD>2018-12-31</TD></TR>" +
                "<TR><TD>2</TD><TD>egg</TD><TD>Prestige</TD><TD>12</TD><TD>items</TD><TD>T</TD><TD>box</TD><TD>2018-12-21</TD><TD>2018-12-28</TD></TR>" +   // Invalid
                "<TR><TD>3</TD><TD>3.25% milk</TD><TD>Metro</TD><TD>Sealtest</TD><TD>1000</TD><TD>ml</TD><TD>T</TD><TD>box</TD><TD>2018-12-10</TD><TD>2018-12-20</TD></TR>" +
                "<TR><TD>5</TD><TD>oyster meat</TD><TD>Metro</TD><TD>Fanny Bay</TD><TD>500</TD><TD>g</TD><TD>T</TD><TD>box</TD><TD>2018-12-26</TD><TD>2018-12-28</TD></TR>" +
                "<TR><TD>6</TD><TD>whole chicken</TD><TD>Lucky Moose</TD><<TD>3</TD><TD>lb</TD><TD>T</TD><TD>box</TD><TD>2018-12-21</TD><TD>2018-12-31</TD></TR>" + // Invalid
                "<TR><TD>8</TD><TD>Orange juice</TD><TD>Loblaws</TD><TD>Dole</TD><TD>1000</TD><TD>ml</TD><TD>T</TD><TD>box</TD><TD>2019-01-07</TD><TD>2019-02-03</TD></TR>" +
                "</TABLE>";

        final Set<PurchaseHistory> convertedObjs = PurchaseHistory.FromHTMLTableStr(htmlTableStr);

        assertEquals(4, convertedObjs.size());
    }

    @Test
    public void testFromHTMLTableStr_emptyTable_returnsEmptySet() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>vendor</TH><TH>brand</TH><TH>content_quantity</TH><TH>content_unit</TH><TH>is_packaged</TH><TH>package_unit</TH><TH>purchase_date</TH><TH>expiry_date</TH></TR>" +
                "</TABLE>";

        final Set<PurchaseHistory> convertedObjs = PurchaseHistory.FromHTMLTableStr(htmlTableStr);

        assertTrue(convertedObjs.isEmpty());
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesAllValid_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>vendor</TH><TH>brand</TH><TH>content_quantity</TH><TH>content_unit</TH><TH>is_packaged</TH><TH>package_unit</TH><TH>purchase_date</TH><TH>expiry_date</TH></TR>" +
                "<TR><TD>1</TD><TD>egg</TD><TD>Metro</TD><TD>Prestige</TD><TD>12</TD><TD>items</TD><TD>T</TD><TD>box</TD><TD>2018-12-21</TD><TD>2018-12-31</TD></TR>" +
                "<TR><TD>2</TD><TD>egg</TD><TD>Metro</TD><TD>Prestige</TD><TD>12</TD><TD>items</TD><TD>T</TD><TD>box</TD><TD>2018-12-21</TD><TD>2018-12-28</TD></TR>" +
                "<TR><TD>3</TD><TD>3.25% milk</TD><TD>Metro</TD><TD>Sealtest</TD><TD>1000</TD><TD>ml</TD><TD>T</TD><TD>box</TD><TD>2018-12-10</TD><TD>2018-12-20</TD></TR>" +
                "</TABLE>" +
                "<TABLE BOARDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>vendor</TH><TH>brand</TH><TH>content_quantity</TH><TH>content_unit</TH><TH>is_packaged</TH><TH>package_unit</TH><TH>purchase_date</TH><TH>expiry_date</TH></TR>" +
                "<TR><TD>5</TD><TD>oyster meat</TD><TD>Metro</TD><TD>Fanny Bay</TD><TD>500</TD><TD>g</TD><TD>T</TD><TD>box</TD><TD>2018-12-26</TD><TD>2018-12-28</TD></TR>" +
                "<TR><TD>6</TD><TD>whole chicken</TD><TD>Lucky Moose</TD><TD>Lucky Moose</TD><TD>3</TD><TD>lb</TD><TD>T</TD><TD>box</TD><TD>2018-12-21</TD><TD>2018-12-31</TD></TR>" +
                "<TR><TD>8</TD><TD>Orange juice</TD><TD>Loblaws</TD><TD>Dole</TD><TD>1000</TD><TD>ml</TD><TD>T</TD><TD>box</TD><TD>2019-01-07</TD><TD>2019-02-03</TD></TR>" +
                "</TABLE>";

        final Set<PurchaseHistory> convertedObjs = PurchaseHistory.FromHTMLTableStr(htmlTableStr);

        assertEquals(6, convertedObjs.size());
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesOneValid_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>std_name</TH><TH>vendor</TH><TH>brand</TH><TH>content_quantity</TH><TH>content_unit</TH><TH>is_packaged</TH><TH>package_unit</TH><TH>purchase_date</TH><TH>expiry_date</TH></TR>" +
                "<TR><TD>1</TD><TD>egg</TD><TD>Metro</TD><TD>Prestige</TD><TD>12</TD><TD>items</TD><TD>T</TD><TD>box</TD><TD>2018-12-21</TD><TD>2018-12-31</TD></TR>" +
                "<TR><TD>2</TD><TD>egg</TD><TD>Metro</TD><TD>Prestige</TD><TD>12</TD><TD>items</TD><TD>T</TD><TD>box</TD><TD>2018-12-21</TD><TD>2018-12-28</TD></TR>" +
                "<TR><TD>3</TD><TD>3.25% milk</TD><TD>Metro</TD><TD>Sealtest</TD><TD>1000</TD><TD>ml</TD><TD>T</TD><TD>box</TD><TD>2018-12-10</TD><TD>2018-12-20</TD></TR>" +
                "</TABLE>" +
                "<TABLE BOARDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>vendor</TH><TH>brand</TH><TH>content_quantity</TH><TH>content_unit</TH><TH>is_packaged</TH><TH>package_unit</TH><TH>purchase_date</TH><TH>expiry_date</TH></TR>" +
                "<TR><TD>5</TD><TD>oyster meat</TD><TD>Metro</TD><TD>Fanny Bay</TD><TD>500</TD><TD>g</TD><TD>T</TD><TD>box</TD><TD>2018-12-26</TD><TD>2018-12-28</TD></TR>" +
                "<TR><TD>6</TD><TD>whole chicken</TD><TD>Lucky Moose</TD><TD>Lucky Moose</TD><TD>3</TD><TD>lb</TD><TD>T</TD><TD>box</TD><TD>2018-12-21</TD><TD>2018-12-31</TD></TR>" +
                "<TR><TD>8</TD><TD>Orange juice</TD><TD>Loblaws</TD><TD>Dole</TD><TD>1000</TD><TD>ml</TD><TD>T</TD><TD>box</TD><TD>2019-01-07</TD><TD>2019-02-03</TD></TR>" +
                "</TABLE>";

        final Set<PurchaseHistory> convertedObjs = PurchaseHistory.FromHTMLTableStr(htmlTableStr);

        assertEquals(3, convertedObjs.size());
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesNoneValid_returnsEmptyTable() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>vendor</TH><TH>content_quantity</TH><TH>content_unit</TH><TH>is_packaged</TH><TH>package_unit</TH><TH>purchase_date</TH><TH>expiry_date</TH></TR>" +
                "<TR><TD>1</TD><TD>egg</TD><TD>Metro</TD><TD>Prestige</TD><TD>12</TD><TD>items</TD><TD>T</TD><TD>box</TD><TD>2018-12-21</TD><TD>2018-12-31</TD></TR>" +
                "<TR><TD>2</TD><TD>egg</TD><TD>Metro</TD><TD>Prestige</TD><TD>12</TD><TD>items</TD><TD>T</TD><TD>box</TD><TD>2018-12-21</TD><TD>2018-12-28</TD></TR>" +
                "<TR><TD>3</TD><TD>3.25% milk</TD><TD>Metro</TD><TD>Sealtest</TD><TD>1000</TD><TD>ml</TD><TD>T</TD><TD>box</TD><TD>2018-12-10</TD><TD>2018-12-20</TD></TR>" +
                "</TABLE>" +
                "<TABLE BOARDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>vendor</TH><TH>brand</TH><TH>content_quantity</TH><TH>is_packaged</TH><TH>package_unit</TH><TH>purchase_date</TH><TH>expiry_date</TH></TR>" +
                "<TR><TD>5</TD><TD>oyster meat</TD><TD>Metro</TD><TD>Fanny Bay</TD><TD>500</TD><TD>g</TD><TD>T</TD><TD>box</TD><TD>2018-12-26</TD><TD>2018-12-28</TD></TR>" +
                "<TR><TD>6</TD><TD>whole chicken</TD><TD>Lucky Moose</TD><TD>Lucky Moose</TD><TD>3</TD><TD>lb</TD><TD>T</TD><TD>box</TD><TD>2018-12-21</TD><TD>2018-12-31</TD></TR>" +
                "<TR><TD>8</TD><TD>Orange juice</TD><TD>Loblaws</TD><TD>Dole</TD><TD>1000</TD><TD>ml</TD><TD>T</TD><TD>box</TD><TD>2019-01-07</TD><TD>2019-02-03</TD></TR>" +
                "</TABLE>";

        final Set<PurchaseHistory> convertedObjs = PurchaseHistory.FromHTMLTableStr(htmlTableStr);

        assertTrue(convertedObjs.isEmpty());
    }


    @Test
    public void testIsValidTableSchema_schemaValid_returnsTrue() {
        final String validTableSchema = "<TABLE BORDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>vendor</TH><TH>brand</TH><TH>content_quantity</TH><TH>content_unit</TH><TH>is_packaged</TH><TH>package_unit</TH><TH>purchase_date</TH><TH>expiry_date</TH></TR>" +
                "<TR><TD>1</TD><TD>egg</TD><TD>Metro</TD><TD>Prestige</TD><TD>12</TD><TD>items</TD><TD>T</TD><TD>box</TD><TD>2018-12-21</TD><TD>2018-12-31</TD></TR>" +
                "<TR><TD>2</TD><TD>egg</TD><TD>Metro</TD><TD>Prestige</TD><TD>12</TD><TD>items</TD><TD>T</TD><TD>box</TD><TD>2018-12-21</TD><TD>2018-12-28</TD></TR>" +
                "<TR><TD>3</TD><TD>3.25% milk</TD><TD>Metro</TD><TD>Sealtest</TD><TD>1000</TD><TD>ml</TD><TD>T</TD><TD>box</TD><TD>2018-12-10</TD><TD>2018-12-20</TD></TR>" +
                "<TR><TD>5</TD><TD>oyster meat</TD><TD>Metro</TD><TD>Fanny Bay</TD><TD>500</TD><TD>g</TD><TD>T</TD><TD>box</TD><TD>2018-12-26</TD><TD>2018-12-28</TD></TR>" +
                "<TR><TD>6</TD><TD>whole chicken</TD><TD>Lucky Moose</TD><TD>Lucky Moose</TD><TD>3</TD><TD>lb</TD><TD>T</TD><TD>box</TD><TD>2018-12-21</TD><TD>2018-12-31</TD></TR>" +
                "<TR><TD>8</TD><TD>Orange juice</TD><TD>Loblaws</TD><TD>Dole</TD><TD>1000</TD><TD>ml</TD><TD>T</TD><TD>box</TD><TD>2019-01-07</TD><TD>2019-02-03</TD></TR>" +
                "</TABLE>";

        final boolean isValidSchema = PurchaseHistory.isValidTableSchema(Jsoup.parse(validTableSchema));

        assertTrue(isValidSchema);
    }

    @Test
    public void testIsValidTableSchema_missingFields_returnsFalse() {
        final String missingFieldsTableSchema = "<TABLE BORDER=1>" +
                "<TR><TH>uid</TH><TH>std_name</TH><TH>vendor</TH><TH>content_quantity</TH><TH>is_packaged</TH><TH>package_unit</TH><TH>purchase_date</TH><TH>expiry_date</TH></TR>" +
                "<TR><TD>1</TD><TD>egg</TD><TD>Metro</TD><TD>12</TD><TD>items</TD><TD>T</TD><TD>2018-12-21</TD><TD>2018-12-31</TD></TR>" +
                "<TR><TD>2</TD><TD>egg</TD><TD>Metro</TD><TD>12</TD><TD>items</TD><TD>T</TD><TD>2018-12-21</TD><TD>2018-12-28</TD></TR>" +
                "<TR><TD>3</TD><TD>3.25% milk</TD><TD>Metro</TD><TD>1000</TD><TD>ml</TD><TD>T</TD><TD>2018-12-10</TD><TD>2018-12-20</TD></TR>" +
                "<TR><TD>5</TD><TD>oyster meat</TD><TD>Metro</TD><TD>500</TD><TD>g</TD><TD>T</TD><TD>2018-12-26</TD><TD>2018-12-28</TD></TR>" +
                "<TR><TD>6</TD><TD>whole chicken</TD><TD>Lucky Moose</TD><TD>3</TD><TD>lb</TD><TD>T</TD><TD>2018-12-21</TD><TD>2018-12-31</TD></TR>" +
                "<TR><TD>8</TD><TD>Orange juice</TD><TD>Loblaws</TD><TD>1000</TD><TD>ml</TD><TD>T</TD><TD>2019-01-07</TD><TD>2019-02-03</TD></TR>" +
                "</TABLE>";

        final boolean isValidSchema = PurchaseHistory.isValidTableSchema(Jsoup.parse(missingFieldsTableSchema));

        assertFalse(isValidSchema);
    }
}