package com.example.zhepingjiang.db;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Business object for Content Units
 */
@Data
@AllArgsConstructor
public class PackageUnits implements putsDBEntry {

    private static String DEFAULT_PACKAGE_UNIT = "default";

    private String unit;

    public PackageUnits() {
        unit = DEFAULT_PACKAGE_UNIT;
    }


    public static String GetTableNameStatic() {
        return "package_units";
    }

    public static String GetDeleteQueryStatic(final String packageUnit) {
        return "delete from " + GetTableNameStatic() + " where unit = '" + packageUnit + "';";
    }

    public static String GetSelectQueryStatic(final String packageUnit) {
        return "select * from " + GetTableNameStatic() + " where unit = '" + packageUnit + "';";
    }

    public static String GetSelectAllQueryStatic() {
        return "select * from " + GetTableNameStatic() + ";";
    }

    public String getTableName() {
        return GetTableNameStatic();
    }

    public String getInsertQuery() {
        return "insert into " + getTableName() + " values ('" + unit + "');";
    }

    public String getUpsertQuery() {
        return "insert ignore into " + getTableName() + " values ('" + unit + "');";
    }

    public String getDeleteQuery() {
        return GetDeleteQueryStatic(unit);
    }

    public String getSelectQuery() {
        return GetSelectQueryStatic(unit);
    }
}
