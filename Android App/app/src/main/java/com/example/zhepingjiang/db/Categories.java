package com.example.zhepingjiang.db;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Business object for Categories
 */
@Data
@AllArgsConstructor
public class Categories implements putsDBEntry {

    private static String DEFAULT_CATEGORY = "uncategorized";

    private String category;

    public Categories() {
        category = DEFAULT_CATEGORY;
    }


    public static String GetTableNameStatic() {
        return "categories";
    }

    public static String GetDeleteQueryStatic(final String category) {
        return "delete from " + GetTableNameStatic() + " where category = '" + category + "';";
    }

    public static String GetSelectQueryStatic(final String category) {
        return "select * from " + GetTableNameStatic() + " where category = '" + category + "';";
    }

    public static String GetSelectAllQueryStatic() {
        return "select * from " + GetTableNameStatic() + ";";
    }

    public String getTableName() {
        return GetTableNameStatic();
    }

    public String getInsertQuery() {
        return "insert into " + getTableName() + " values ('" + category + "');";
    }

    public String getUpsertQuery() {
        return "insert ignore into " + getTableName() + " values ('" + category + "');";
    }

    public String getDeleteQuery() {
        return GetDeleteQueryStatic(category);
    }

    public String getSelectQuery() {
        return GetSelectQueryStatic(category);
    }
}
