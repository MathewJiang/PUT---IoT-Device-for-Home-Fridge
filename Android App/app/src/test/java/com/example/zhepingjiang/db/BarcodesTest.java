package com.example.zhepingjiang.db;

import org.junit.Test;

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
        final char defaultIsPackaged = 'T';
        final ContentUnits defaultContentUnit = new ContentUnits();
        final PackageUnits defaultPackageUnit = new PackageUnits();

        final Barcodes defaultObj = new Barcodes(barcode, stdName);

        assertEquals(barcode, defaultObj.getBarcode());
        assertEquals(stdName.getStdName(), defaultObj.getStdName().getStdName());
        assertEquals(defaultBrand, defaultObj.getBrand());
        assertEquals(defaultContentQuantity, defaultObj.getContentQuantity());
        assertEquals(defaultIsPackaged, defaultObj.getIsPackaged());
        assertEquals(defaultContentUnit, defaultObj.getContentUnit());
        assertEquals(defaultPackageUnit, defaultObj.getPackageUnit());
    }

    @Test
    public void testBuildBarcodes_allArgsConstr_withSpecifiedFields() {
        final StdNames stdName = new StdNames("Orange juice");
        final String barcode = "01234567";
        final Brands brand = new Brands("Dole");
        final int contentQuantity = 1000;
        final char isPackaged = 'T';
        final ContentUnits contentUnit = new ContentUnits("ml");
        final PackageUnits packageUnit = new PackageUnits("box");

        final Barcodes allArgsObj = new Barcodes(barcode, brand, stdName, contentQuantity, contentUnit, isPackaged, packageUnit);

        assertEquals(barcode, allArgsObj.getBarcode());
        assertEquals(stdName.getStdName(), allArgsObj.getStdName().getStdName());
        assertEquals(brand, allArgsObj.getBrand());
        assertEquals(contentQuantity, allArgsObj.getContentQuantity());
        assertEquals(isPackaged, allArgsObj.getIsPackaged());
        assertEquals(contentUnit, allArgsObj.getContentUnit());
        assertEquals(packageUnit, allArgsObj.getPackageUnit());
    }

    @Test
    public void testGetInsertQuery_withDefaultFields_happyPath() {
        final StdNames stdName = new StdNames("Orange juice");
        final String barcode = "01234567";
        final Brands defaultBrand = new Brands();
        final int defaultContentQuantity = 0;
        final char defaultIsPackaged = 'T';
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
        final char isPackaged = 'T';
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
        final char defaultIsPackaged = 'T';
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
        final char isPackaged = 'T';
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
}