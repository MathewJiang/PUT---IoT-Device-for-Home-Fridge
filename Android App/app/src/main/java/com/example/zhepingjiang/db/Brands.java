package com.example.zhepingjiang.db;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Business object for Brands
 */
@Data
@AllArgsConstructor
public class Brands implements putsDBEntry {

    private static String DEFAULT_BRAND = "default";

    private String brandName;

    public Brands() {
        brandName = DEFAULT_BRAND;
    }


    public static String GetTableNameStatic() {
        return "brands";
    }

    public static String GetDeleteQueryStatic(final String brandName) {
        return "delete from " + GetTableNameStatic() + " where brand_name = '" + brandName + "';";
    }

    public static String GetSelectQueryStatic(final String brandName) {
        return "select * from " + GetTableNameStatic() + " where brand_name = '" + brandName + "';";
    }

    public static String GetSelectAllQueryStatic() {
        return "select * from " + GetTableNameStatic() + ";";
    }

    public String getTableName() {
        return GetTableNameStatic();
    }

    public String getInsertQuery() {
        return "insert into " + getTableName() + " values ('" + brandName + "');";
    }

    public String getUpsertQuery() {
        return "insert ignore into " + getTableName() + " values ('" + brandName + "');";
    }

    public String getDeleteQuery() {
        return GetDeleteQueryStatic(brandName);
    }

    public String getSelectQuery() {
        return GetSelectQueryStatic(brandName);
    }
}
