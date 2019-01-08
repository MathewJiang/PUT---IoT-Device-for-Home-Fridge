package com.example.zhepingjiang.db;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Business object for Content Units
 */
@Data
@AllArgsConstructor
public class ContentUnits implements putsDBEntry {

    public static String DEFAULT_CONTENT_UNIT = "default";

    private String unit;

    public ContentUnits() {
        unit = DEFAULT_CONTENT_UNIT;
    }


    public static String GetTableNameStatic() {
        return "content_units";
    }

    public static String GetDeleteQueryStatic(final String contentUnit) {
        return "delete from " + GetTableNameStatic() + " where unit = '" + contentUnit + "';";
    }

    public static String GetSelectQueryStatic(final String contentUnit) {
        return "select * from " + GetTableNameStatic() + " where unit = '" + contentUnit + "';";
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