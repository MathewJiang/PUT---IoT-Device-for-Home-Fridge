package com.example.zhepingjiang.db;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Business Object for Grocery Storage.
 */
@Data
@AllArgsConstructor
public class GroceryStorage {

    private int uid;
    private StdNames stdName;
    private int contentQuantity;
    private ContentUnits contentUnit;
    private String lastUpdatedTimeStamp;
    private String purchaseDate;
    private String expiryDate;
    private Statuses status;

    // Constructor that builds a new storage entry from a purchase entry.
    public GroceryStorage(final PurchaseHistory ph, final Statuses _status, final String _lastUpdatedTimeStamp) {
        uid = ph.getUid();
        stdName = ph.getStdName();
        contentQuantity = ph.getContentQuantity();
        contentUnit = ph.getContentUnit();
        lastUpdatedTimeStamp = _lastUpdatedTimeStamp;
        purchaseDate = ph.getPurchaseDate();
        expiryDate = ph.getExpiryDate();
        status = _status;
    }

    public static String GetTableNameStatic() {
        return "grocery_storage";
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

    public String getInsertQuery() {
        return stdName.getUpsertQuery() +
                contentUnit.getUpsertQuery() +
                status.getUpsertQuery() +
                "insert into " + getTableName() + " values (" + uid + ",'" + stdName.getStdName() + "'," +
                    contentQuantity + ",'" + contentUnit.getUnit() + "','" + lastUpdatedTimeStamp + "','" +
                    purchaseDate + "','" + expiryDate + "','" + status.getStatus() +  "');";
    }

    public String getUpsertQuery() {
        return stdName.getUpsertQuery() +
                contentUnit.getUpsertQuery() +
                status.getUpsertQuery() +
                "insert ignore into " + getTableName() + " values (" + uid + ",'" + stdName.getStdName() + "'," +
                contentQuantity + ",'" + contentUnit.getUnit() + "','" + lastUpdatedTimeStamp + "','" +
                purchaseDate + "','" + expiryDate + "','" + status.getStatus() +  "');";
    }

    public String getDeleteQuery() {
        return GetDeleteQueryStatic(uid);
    }

    public String getSelectQuery() {
        return GetSelectQueryStatic(uid);
    }

}
