package com.example.zhepingjiang.db;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Business object for Barcodes
 */
@Data
@AllArgsConstructor
public class Barcodes implements putsDBEntry {

    private String barcode;
    private Brands brand;
    private StdNames stdName;
    private int contentQuantity;
    private ContentUnits contentUnit;
    private char isPackaged;
    private PackageUnits packageUnit;

    public Barcodes (final String _barcode, final StdNames _stdName) {
        barcode = _barcode;
        stdName = _stdName;
        // Default values for the rest, use setters to set them.
        brand = new Brands();
        contentQuantity = 0;
        contentUnit = new ContentUnits();
        isPackaged = 'T';
        packageUnit = new PackageUnits();
    }


    public static String GetTableNameStatic() {
        return "barcodes";
    }

    public static String GetDeleteQueryStatic(final String barcode) {
        return "delete from " + GetTableNameStatic() + " where barcode = '" + barcode + "';";
    }

    public static String GetSelectQueryStatic(final String barcode) {
        return "select * from " + GetTableNameStatic() + " where barcode = '" + barcode + "';";
    }

    public static String GetSelectAllQueryStatic() {
        return "select * from " + GetTableNameStatic() + ";";
    }

    public String getTableName() {
        return GetTableNameStatic();
    }

    public String getInsertQuery() {
        return stdName.getUpsertQuery() +
                brand.getUpsertQuery() +
                contentUnit.getUpsertQuery() +
                packageUnit.getUpsertQuery() +
                "insert into " + getTableName() + " values ('" + barcode + "','" + brand.getBrandName() + "','" +
                    stdName.getStdName() + "'," + contentQuantity + ",'" + contentUnit.getUnit() + "','" + isPackaged + "','" + packageUnit.getUnit() + "');";
    }

    public String getUpsertQuery() {
        return stdName.getUpsertQuery() +
                brand.getUpsertQuery() +
                contentUnit.getUpsertQuery() +
                packageUnit.getUpsertQuery() +
                "insert ignore into " + getTableName() + " values ('" + barcode + "','" + brand.getBrandName() + "','" +
                stdName.getStdName() + "'," + contentQuantity + ",'" + contentUnit.getUnit() + "','" + isPackaged + "','" + packageUnit.getUnit() + "');";
    }

    public String getDeleteQuery() {
        return GetDeleteQueryStatic(barcode);
    }

    public String getSelectQuery() {
        return GetSelectQueryStatic(barcode);
    }
}