package com.example.zhepingjiang.db;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Business object for Content Units.
 */
@Data
@AllArgsConstructor
public class Vendors implements putsDBEntry {

    private static String DEFAULT_VENDOR = "default";

    private String vendorName;

    public Vendors() {
        vendorName = DEFAULT_VENDOR;
    }


    public static String GetTableNameStatic() {
        return "vendors";
    }

    public static String GetDeleteQueryStatic(final String vendorName) {
        return "delete from " + GetTableNameStatic() + " where vendor_name = '" + vendorName + "';";
    }

    public static String GetSelectQueryStatic(final String vendorName) {
        return "select * from " + GetTableNameStatic() + " where vendor_name = '" + vendorName + "';";
    }

    public static String GetSelectAllQueryStatic() {
        return "select * from " + GetTableNameStatic() + ";";
    }

    public String getTableName() {
        return GetTableNameStatic();
    }

    public String getInsertQuery() {
        return "insert into " + getTableName() + " values ('" + vendorName + "');";
    }

    public String getUpsertQuery() {
        return "insert ignore into " + getTableName() + " values ('" + vendorName + "');";
    }

    public String getDeleteQuery() {
        return GetDeleteQueryStatic(vendorName);
    }

    public String getSelectQuery() {
        return GetSelectQueryStatic(vendorName);
    }
}
