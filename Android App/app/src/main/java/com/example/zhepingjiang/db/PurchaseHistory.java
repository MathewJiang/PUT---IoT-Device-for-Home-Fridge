package com.example.zhepingjiang.db;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Business Object for Purchase History.
 */
@Data
@AllArgsConstructor
public class PurchaseHistory implements putsDBEntry {

    private int uid;
    private StdNames stdName;
    private Vendors vendor;
    private Brands brand;
    private int contentQuantity;
    private ContentUnits contentUnit;
    private char isPackaged;
    private PackageUnits packageUnit;
    private String purchaseDate;
    private String expiryDate;

    public PurchaseHistory(final StdNames _stdName) {
        uid = 0;
        stdName = _stdName;
        // Assign default values to rest of fields. Set them via setters.
        vendor = new Vendors();
        brand = new Brands();
        contentQuantity = 0;
        contentUnit = new ContentUnits();
        isPackaged = 'F';
        packageUnit = new PackageUnits();
        purchaseDate = "2019-01-01";
        expiryDate = "2019-01-02";
    }

    public PurchaseHistory(final StdNames _stdName, final Vendors _vendor, final Brands _brand,
                           final int _contentQuantity, final ContentUnits _contentUnit, final char _isPackaged,
                           final PackageUnits _packageUnits, final String _purchaseDate, final String _expiryDate) {
        uid = 0;
        stdName = _stdName;
        vendor = _vendor;
        brand = _brand;
        contentQuantity = _contentQuantity;
        contentUnit = _contentUnit;
        isPackaged = _isPackaged;
        packageUnit = _packageUnits;
        purchaseDate = _purchaseDate;
        expiryDate = _expiryDate;
    }


    public static String GetTableNameStatic() {
        return "purchase_history";
    }

    public static String GetDeleteQueryStatic(final int uid) {
        return "delete from " + GetTableNameStatic() + " where uid = " + uid + ";";
    }

    public static String GetSelectQueryStatic(final int uid) {
        return "select * from " + GetTableNameStatic() + " where uid = " + uid + ";";
    }

    public static String GetSelectAllQueryStatic() {
        return "select * from " + GetTableNameStatic() + ";";
    }

    public String getTableName() {
        return GetTableNameStatic();
    }

    public String getPartialFieldsStr() { return "(std_name, vendor, brand, content_quantity, content_unit, is_packaged, package_unit, purchase_date, expiry_date)"; }

    public String getInsertQuery() {
        return stdName.getUpsertQuery() +
                vendor.getUpsertQuery() +
                brand.getUpsertQuery() +
                contentUnit.getUpsertQuery() +
                packageUnit.getUpsertQuery() +
                "insert into " + getTableName() + " " + getPartialFieldsStr() + " values ('" + stdName.getStdName() + "','" +
                    vendor.getVendorName() + "','" + brand.getBrandName() + "'," + contentQuantity + ",'" +
                    contentUnit.getUnit() + "','" + isPackaged + "','" + packageUnit.getUnit() + "','" + purchaseDate + "','" + expiryDate +  "');";
    }

    public String getUpsertQuery() {
        return stdName.getUpsertQuery() +
                vendor.getUpsertQuery() +
                brand.getUpsertQuery() +
                contentUnit.getUpsertQuery() +
                packageUnit.getUpsertQuery() +
                "insert ignore into " + getTableName() + " " + getPartialFieldsStr() + " values ('" + stdName.getStdName() + "','" +
                    vendor.getVendorName() + "','" + brand.getBrandName() + "'," + contentQuantity + ",'" +
                    contentUnit.getUnit() + "','" + isPackaged + "','" + packageUnit.getUnit() + "','" + purchaseDate + "','" + expiryDate +  "');";
    }

    public String getDeleteQuery() {
        return GetDeleteQueryStatic(uid);
    }

    public String getSelectQuery() {
        return GetSelectQueryStatic(uid);
    }
}
