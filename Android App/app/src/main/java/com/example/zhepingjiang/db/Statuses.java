package com.example.zhepingjiang.db;

import lombok.AllArgsConstructor;
import lombok.Data;

/*
 * Business object for Barcodes
 */
@Data
@AllArgsConstructor
public class Statuses implements putsDBEntry {

    private static String DEFAULT_STATUS = "default";

    private String status;

    public Statuses() {
        status = DEFAULT_STATUS;
    }


    public static String GetTableNameStatic() {
        return "statuses";
    }

    public static String GetDeleteQueryStatic(final String status) {
        return "delete from " + GetTableNameStatic() + " where status = '" + status + "';";
    }

    public static String GetSelectQueryStatic(final String status) {
        return "select * from " + GetTableNameStatic() + " where status = '" + status + "';";
    }

    public static String GetSelectAllQueryStatic() {
        return "select * from " + GetTableNameStatic() + ";";
    }

    public String getTableName() {
        return GetTableNameStatic();
    }

    public String getInsertQuery() {
        return "insert into " + getTableName() + " values ('" + status + "');";
    }

    public String getUpsertQuery() {
        return "insert ignore into " + getTableName() + " values ('" + status + "');";
    }

    public String getDeleteQuery() {
        return GetDeleteQueryStatic(status);
    }

    public String getSelectQuery() {
        return GetSelectQueryStatic(status);
    }
}
