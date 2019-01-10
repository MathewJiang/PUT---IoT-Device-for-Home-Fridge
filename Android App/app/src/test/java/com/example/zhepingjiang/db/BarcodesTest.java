package com.example.zhepingjiang.db;

import com.google.common.collect.Lists;

import org.jsoup.Jsoup;
import org.junit.Test;
import org.junit.Ignore;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Barcodes Table Unit Tests.
 */
public class BarcodesTest {

    @Test
    public void testGetTableName_happyPath() {
        final String expectedTableName = "barcodes";

        final String actualTableName = new Barcodes("0123456", new StdNames("Orange juice")).getTableName();

        assertEquals(expectedTableName, actualTableName);
    }

    @Test
    public void testBuildBarcodes_partialConstr_withDefaultFields() {
        final StdNames stdName = new StdNames("Orange juice");
        final String barcode = "01234567";
        final Brands defaultBrand = new Brands();
        final int defaultContentQuantity = 0;
        final boolean defaultIsPackaged = true;
        final ContentUnits defaultContentUnit = new ContentUnits();
        final PackageUnits defaultPackageUnit = new PackageUnits();

        final Barcodes defaultObj = new Barcodes(barcode, stdName);

        assertEquals(barcode, defaultObj.getBarcode());
        assertEquals(stdName.getStdName(), defaultObj.getStdName().getStdName());
        assertEquals(defaultBrand, defaultObj.getBrand());
        assertEquals(defaultContentQuantity, defaultObj.getContentQuantity());
        assertEquals(defaultIsPackaged, defaultObj.isPackaged());
        assertEquals(defaultContentUnit, defaultObj.getContentUnit());
        assertEquals(defaultPackageUnit, defaultObj.getPackageUnit());
    }

    @Test
    public void testBuildBarcodes_allArgsConstr_withSpecifiedFields() {
        final StdNames stdName = new StdNames("Orange juice");
        final String barcode = "01234567";
        final Brands brand = new Brands("Dole");
        final int contentQuantity = 1000;
        final boolean isPackaged = true;
        final ContentUnits contentUnit = new ContentUnits("ml");
        final PackageUnits packageUnit = new PackageUnits("box");

        final Barcodes allArgsObj = new Barcodes(barcode, brand, stdName, contentQuantity, contentUnit, isPackaged, packageUnit);

        assertEquals(barcode, allArgsObj.getBarcode());
        assertEquals(stdName.getStdName(), allArgsObj.getStdName().getStdName());
        assertEquals(brand, allArgsObj.getBrand());
        assertEquals(contentQuantity, allArgsObj.getContentQuantity());
        assertEquals(isPackaged, allArgsObj.isPackaged());
        assertEquals(contentUnit, allArgsObj.getContentUnit());
        assertEquals(packageUnit, allArgsObj.getPackageUnit());
    }

    @Test
    public void testGetInsertQuery_withDefaultFields_happyPath() {
        final StdNames stdName = new StdNames("Orange juice");
        final String barcode = "01234567";
        final Brands defaultBrand = new Brands();
        final int defaultContentQuantity = 0;
        final boolean defaultIsPackaged = true;
        final ContentUnits defaultContentUnit = new ContentUnits();
        final PackageUnits defaultPackageUnit = new PackageUnits();
        final String expectedQuery = stdName.getUpsertQuery() +
                defaultBrand.getUpsertQuery() +
                defaultContentUnit.getUpsertQuery() +
                defaultPackageUnit.getUpsertQuery() +
                "insert into barcodes values ('01234567','default','Orange juice',0,'default','T','default');";

        final String actualQuery = new Barcodes(barcode, defaultBrand, stdName, defaultContentQuantity,
                defaultContentUnit, defaultIsPackaged, defaultPackageUnit).getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetInsertQuery_allFieldsSpecified_happyPath() {
        final StdNames stdName = new StdNames("Orange juice");
        final String barcode = "01234567";
        final Brands brand = new Brands("Dole");
        final int contentQuantity = 1000;
        final boolean isPackaged = true;
        final ContentUnits contentUnit = new ContentUnits("ml");
        final PackageUnits packageUnit = new PackageUnits("box");
        final String expectedQuery = stdName.getUpsertQuery() +
                brand.getUpsertQuery() +
                contentUnit.getUpsertQuery() +
                packageUnit.getUpsertQuery() +
                "insert into barcodes values ('01234567','Dole','Orange juice',1000,'ml','T','box');";

        final String actualQuery = new Barcodes(barcode, brand, stdName, contentQuantity, contentUnit,
                isPackaged, packageUnit).getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_withDefaultFields_happyPath() {
        final StdNames stdName = new StdNames("Orange juice");
        final String barcode = "01234567";
        final Brands defaultBrand = new Brands();
        final int defaultContentQuantity = 0;
        final boolean defaultIsPackaged = true;
        final ContentUnits defaultContentUnit = new ContentUnits();
        final PackageUnits defaultPackageUnit = new PackageUnits();
        final String expectedQuery = stdName.getUpsertQuery() +
                defaultBrand.getUpsertQuery() +
                defaultContentUnit.getUpsertQuery() +
                defaultPackageUnit.getUpsertQuery() +
                "insert ignore into barcodes values ('01234567','default','Orange juice',0,'default','T','default');";

        final String actualQuery = new Barcodes(barcode, defaultBrand, stdName, defaultContentQuantity,
                defaultContentUnit, defaultIsPackaged, defaultPackageUnit).getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_allFieldsSpecified_happyPath() {
        final StdNames stdName = new StdNames("Orange juice");
        final String barcode = "01234567";
        final Brands brand = new Brands("Dole");
        final int contentQuantity = 1000;
        final boolean isPackaged = true;
        final ContentUnits contentUnit = new ContentUnits("ml");
        final PackageUnits packageUnit = new PackageUnits("box");
        final String expectedQuery = stdName.getUpsertQuery() +
                brand.getUpsertQuery() +
                contentUnit.getUpsertQuery() +
                packageUnit.getUpsertQuery() +
                "insert ignore into barcodes values ('01234567','Dole','Orange juice',1000,'ml','T','box');";

        final String actualQuery = new Barcodes(barcode, brand, stdName, contentQuantity, contentUnit,
                isPackaged, packageUnit).getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQuery_happyPath() {
        final String expectedQuery = "delete from barcodes where barcode = '01234567';";

        final String actualQuery = new Barcodes("01234567", new StdNames("Orange juice")).getDeleteQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQuery_happyPath() {
        final String expectedQuery = "select * from barcodes where barcode = '01234567';";

        final String actualQuery = new Barcodes("01234567", new StdNames("Orange juice")).getSelectQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQueryStatic_happyPath() {
        final String expectedQuery = "delete from barcodes where barcode = '01234567';";

        final String actualQuery = new Barcodes("01234567", new StdNames("Orange juice")).getDeleteQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQueryStatic_happyPath() {
        final String expectedQuery = "select * from barcodes where barcode = '01234567';";

        final String actualQuery = Barcodes.GetSelectQueryStatic("01234567");

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectAllQueryStatic_happyPath() {
        final String expectedQuery = "select * from barcodes;";

        final String actualQuery = Barcodes.GetSelectAllQueryStatic();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testFromHTMLTableStr_singleEntry_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>barcode</TH><TH>brand</TH><TH>std_name</TH><TH>content_quantity</TH><TH>content_unit</TH><TH>is_packaged</TH><TH>package_unit</TH></TR>" +
                "<TR><TD>01234567</TD><TD>Dole</TD><TD>Orange juice</TD><TD>1000</TD><TD>ml</TD><TD>T</TD><TD>box</TD></TR>" +
                "</TABLE>";

        final Set<Barcodes> convertedObjs = Barcodes.FromHTMLTableStr(htmlTableStr);

        assertEquals(1, convertedObjs.size());
        final Barcodes convertedObj = Lists.newArrayList(convertedObjs).get(0);
        assertEquals("01234567", convertedObj.getBarcode());
        assertEquals("Dole", convertedObj.getBrand().getBrandName());
        assertEquals("Orange juice", convertedObj.getStdName().getStdName());
        assertEquals(1000, convertedObj.getContentQuantity());
        assertEquals("ml", convertedObj.getContentUnit().getUnit());
        assertTrue(convertedObj.isPackaged());
        assertEquals("box", convertedObj.getPackageUnit().getUnit());
    }

    @Test
    public void testFromHTMLTableStr_multipleEntries_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>barcode</TH><TH>brand</TH><TH>std_name</TH><TH>content_quantity</TH><TH>content_unit</TH><TH>is_packaged</TH><TH>package_unit</TH></TR>" +
                "<TR><TD>01234567</TD><TD>Dole</TD><TD>Orange juice</TD><TD>1000</TD><TD>ml</TD><TD>T</TD><TD>box</TD></TR>" +
                "<TR><TD>053582317029</TD><TD>Lucky moose</TD><TD>whole chicken</TD><TD>2</TD><TD>lb</TD><TD>T</TD><TD>bag</TD></TR>" +
                "<TR><TD>055872102895</TD><TD>Natrel</TD><TD>10% cream</TD><TD>500</TD><TD>ml</TD><TD>T</TD><TD>box</TD></TR>" +
                "<TR><TD>064420000224</TD><TD>Sealtest</TD><TD>3.25% milk</TD><TD>1000</TD><TD>ml</TD><TD>T</TD><TD>box</TD></TR>" +
                "<TR><TD>064767342001</TD><TD>Prestige</TD><TD>egg</TD><TD>12</TD><TD>items</TD><TD>T</TD><TD>box</TD></TR>" +
                "<TR><TD>073729462836</TD><TD>Fanny Bay</TD><TD>oyster meat</TD><TD>500</TD><TD>g</TD><TD>T</TD><TD>box</TD></TR>" +
                "</TABLE>";

        final Set<Barcodes> convertedObjs = Barcodes.FromHTMLTableStr(htmlTableStr);

        assertEquals(6, convertedObjs.size());
    }

    @Test
    public void testFromHTMLTableStr_severalRowsMissingFields_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>barcode</TH><TH>brand</TH><TH>std_name</TH><TH>content_quantity</TH><TH>content_unit</TH><TH>is_packaged</TH><TH>package_unit</TH></TR>" +
                "<TR><TD>01234567</TD><TD>Dole</TD><TD>Orange juice</TD><TD>1000</TD><TD>ml</TD><TD>T</TD><TD>box</TD></TR>" +
                "<TR><TD>053582317029</TD><TD>Lucky moose</TD><TD>whole chicken</TD><TD>2</TD><TD>lb</TD><TD>T</TD><TD>bag</TD></TR>" +
                "<TR><TD>055872102895</TD><TD>10% cream</TD><TD>500</TD><TD>ml</TD><TD>T</TD><TD>box</TD></TR>" +   // Invalid
                "<TR><TD>064420000224</TD><TD>Sealtest</TD><TD>3.25% milk</TD><TD>1000</TD><TD>ml</TD><TD>T</TD><TD>box</TD></TR>" +
                "<TR><TD>064767342001</TD><TD>Prestige</TD><TD>egg</TD><TD>12</TD><TD>T</TD><TD>box</TD></TR>" +    // Invalid
                "<TR><TD>073729462836</TD><TD>Fanny Bay</TD><TD>oyster meat</TD><TD>500</TD><TD>g</TD><TD>T</TD><TD>box</TD></TR>" +
                "</TABLE>";

        final Set<Barcodes> convertedObjs = Barcodes.FromHTMLTableStr(htmlTableStr);

        assertEquals(4, convertedObjs.size());
    }

    @Test
    public void testFromHTMLTableStr_emptyTable_returnsEmptySet() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>barcode</TH><TH>brand</TH><TH>std_name</TH><TH>content_quantity</TH><TH>content_unit</TH><TH>is_packaged</TH><TH>package_unit</TH></TR>" +
                "</TABLE>";

        final Set<Barcodes> convertedObjs = Barcodes.FromHTMLTableStr(htmlTableStr);

        assertTrue(convertedObjs.isEmpty());
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesAllValid_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>barcode</TH><TH>brand</TH><TH>std_name</TH><TH>content_quantity</TH><TH>content_unit</TH><TH>is_packaged</TH><TH>package_unit</TH></TR>" +
                "<TR><TD>01234567</TD><TD>Dole</TD><TD>Orange juice</TD><TD>1000</TD><TD>ml</TD><TD>T</TD><TD>box</TD></TR>" +
                "<TR><TD>053582317029</TD><TD>Lucky moose</TD><TD>whole chicken</TD><TD>2</TD><TD>lb</TD><TD>T</TD><TD>bag</TD></TR>" +
                "<TR><TD>055872102895</TD><TD>Natrel</TD><TD>10% cream</TD><TD>500</TD><TD>ml</TD><TD>T</TD><TD>box</TD></TR>" +
                "</TABLE>" +
                "<TABLE BOARDER=1>" +
                "<TR><TH>barcode</TH><TH>brand</TH><TH>std_name</TH><TH>content_quantity</TH><TH>content_unit</TH><TH>is_packaged</TH><TH>package_unit</TH></TR>" +
                "<TR><TD>064420000224</TD><TD>Sealtest</TD><TD>3.25% milk</TD><TD>1000</TD><TD>ml</TD><TD>T</TD><TD>box</TD></TR>" +
                "<TR><TD>064767342001</TD><TD>Prestige</TD><TD>egg</TD><TD>12</TD><TD>items</TD><TD>T</TD><TD>box</TD></TR>" +
                "<TR><TD>073729462836</TD><TD>Fanny Bay</TD><TD>oyster meat</TD><TD>500</TD><TD>g</TD><TD>T</TD><TD>box</TD></TR>" +
                "</TABLE>";

        final Set<Barcodes> convertedObjs = Barcodes.FromHTMLTableStr(htmlTableStr);

        assertEquals(6, convertedObjs.size());
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesOneValid_happyPath() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>barcode</TH><TH>brand</TH><TH>std_name</TH><TH>content_quantity</TH><TH>content_unit</TH><TH>is_packaged</TH><TH>package_unit</TH></TR>" +
                "<TR><TD>01234567</TD><TD>Dole</TD><TD>Orange juice</TD><TD>1000</TD><TD>ml</TD><TD>T</TD><TD>box</TD></TR>" +
                "<TR><TD>053582317029</TD><TD>Lucky moose</TD><TD>whole chicken</TD><TD>2</TD><TD>lb</TD><TD>T</TD><TD>bag</TD></TR>" +
                "<TR><TD>055872102895</TD><TD>Natrel</TD><TD>10% cream</TD><TD>500</TD><TD>ml</TD><TD>T</TD><TD>box</TD></TR>" +
                "</TABLE>" +
                "<TABLE BOARDER=1>" +
                "<TR><TH>barcode</TH><TH>brand</TH><TH>std_name</TH><TH>content_quantity</TH><TH>content_unit</TH><TH>is_packaged</TH></TR>" +
                "<TR><TD>064420000224</TD><TD>Sealtest</TD><TD>3.25% milk</TD><TD>1000</TD><TD>ml</TD><TD>T</TD><TD>box</TD></TR>" +
                "<TR><TD>064767342001</TD><TD>Prestige</TD><TD>egg</TD><TD>12</TD><TD>items</TD><TD>T</TD><TD>box</TD></TR>" +
                "<TR><TD>073729462836</TD><TD>Fanny Bay</TD><TD>oyster meat</TD><TD>500</TD><TD>g</TD><TD>T</TD><TD>box</TD></TR>" +
                "</TABLE>";

        final Set<Barcodes> convertedObjs = Barcodes.FromHTMLTableStr(htmlTableStr);

        assertEquals(3, convertedObjs.size());
    }

    @Test
    public void testFromHTMLTableStr_multipleTablesNoneValid_returnsEmptyTable() {
        final String htmlTableStr = "<TABLE BORDER=1>" +
                "<TR><TH>barcode</TH><TH>brand</TH><TH>std_name</TH><TH>content_quantity</TH><TH>content_unit</TH><TH>package_unit</TH></TR>" +
                "<TR><TD>01234567</TD><TD>Dole</TD><TD>Orange juice</TD><TD>1000</TD><TD>ml</TD><TD>T</TD><TD>box</TD></TR>" +
                "<TR><TD>053582317029</TD><TD>Lucky moose</TD><TD>whole chicken</TD><TD>2</TD><TD>lb</TD><TD>T</TD><TD>bag</TD></TR>" +
                "<TR><TD>055872102895</TD><TD>Natrel</TD><TD>10% cream</TD><TD>500</TD><TD>ml</TD><TD>T</TD><TD>box</TD></TR>" +
                "</TABLE>" +
                "<TABLE BOARDER=1>" +
                "<TR><TH>barcode</TH><TH>brand</TH><TH>std_name</TH><TH>content_quantity</TH><TH>content_unit</TH><TH>is_packaged</TH></TR>" +
                "<TR><TD>064420000224</TD><TD>Sealtest</TD><TD>3.25% milk</TD><TD>1000</TD><TD>ml</TD><TD>T</TD><TD>box</TD></TR>" +
                "<TR><TD>064767342001</TD><TD>Prestige</TD><TD>egg</TD><TD>12</TD><TD>items</TD><TD>T</TD><TD>box</TD></TR>" +
                "<TR><TD>073729462836</TD><TD>Fanny Bay</TD><TD>oyster meat</TD><TD>500</TD><TD>g</TD><TD>T</TD><TD>box</TD></TR>" +
                "</TABLE>";

        final Set<Barcodes> convertedObjs = Barcodes.FromHTMLTableStr(htmlTableStr);

        assertTrue(convertedObjs.isEmpty());
    }


    @Test
    public void testIsValidTableSchema_schemaValid_returnsTrue() {
        final String validTableSchema = "<TABLE BORDER=1>" +
                "<TR><TH>barcode</TH><TH>brand</TH><TH>std_name</TH><TH>content_quantity</TH><TH>content_unit</TH><TH>is_packaged</TH><TH>package_unit</TH></TR>" +
                "<TR><TD>01234567</TD><TD>Dole</TD><TD>Orange juice</TD><TD>1000</TD><TD>ml</TD><TD>T</TD><TD>box</TD></TR>" +
                "<TR><TD>053582317029</TD><TD>Lucky moose</TD><TD>whole chicken</TD><TD>2</TD><TD>lb</TD><TD>T</TD><TD>bag</TD></TR>" +
                "<TR><TD>055872102895</TD><TD>Natrel</TD><TD>10% cream</TD><TD>500</TD><TD>ml</TD><TD>T</TD><TD>box</TD></TR>" +
                "<TR><TD>064420000224</TD><TD>Sealtest</TD><TD>3.25% milk</TD><TD>1000</TD><TD>ml</TD><TD>T</TD><TD>box</TD></TR>" +
                "<TR><TD>064767342001</TD><TD>Prestige</TD><TD>egg</TD><TD>12</TD><TD>items</TD><TD>T</TD><TD>box</TD></TR>" +
                "<TR><TD>073729462836</TD><TD>Fanny Bay</TD><TD>oyster meat</TD><TD>500</TD><TD>g</TD><TD>T</TD><TD>box</TD></TR>" +
                "</TABLE>";

        final boolean isValidSchema = Barcodes.isValidTableSchema(Jsoup.parse(validTableSchema));

        assertTrue(isValidSchema);
    }

    @Test
    public void testIsValidTableSchema_missingFields_returnsFalse() {
        final String missingFieldsTableSchema = "<TABLE BORDER=1>" +
                "<TR><TH>barcode</TH><TH>brand</TH><TH>std_name</TH><TH>content_unit</TH><TH>is_packaged</TH><TH>package_unit</TH></TR>" +
                "<TR><TD>01234567</TD><TD>Dole</TD><TD>Orange juice</TD><TD>ml</TD><TD>T</TD><TD>box</TD></TR>" +
                "<TR><TD>053582317029</TD><TD>Lucky moose</TD><TD>whole chicken</TD><TD>lb</TD><TD>T</TD><TD>bag</TD></TR>" +
                "<TR><TD>055872102895</TD><TD>Natrel</TD><TD>10% cream</TD><TD>ml</TD><TD>T</TD><TD>box</TD></TR>" +
                "</TABLE>";

        final boolean isValidSchema = Barcodes.isValidTableSchema(Jsoup.parse(missingFieldsTableSchema));

        assertFalse(isValidSchema);
    }
}