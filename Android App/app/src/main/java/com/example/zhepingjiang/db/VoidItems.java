package com.example.zhepingjiang.db;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Business Object for Void Items.
 */
@Data
@AllArgsConstructor
public class VoidItems implements putsDBEntry {

    private int uid;
    private StdNames stdName;
    private Statuses voidStatus;
    private String timeStamp;

    // Constructor is generated in @AllArgsConstructor Annotation.

    public static String GetTableNameStatic() {
        return "void_items";
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
                voidStatus.getUpsertQuery() +
                "insert into " + getTableName() + " values (" + uid + ",'" + stdName.getStdName() + "','" +
                    voidStatus.getStatus() + "','" + timeStamp + "');";
    }

    public String getUpsertQuery() {
        return stdName.getUpsertQuery() +
                voidStatus.getUpsertQuery() +
                "insert ignore into " + getTableName() + " values (" + uid + ",'" + stdName.getStdName() + "','" +
                voidStatus.getStatus() + "','" + timeStamp + "');";
    }

    public String getDeleteQuery() {
        return GetDeleteQueryStatic(uid);
    }

    public String getSelectQuery() {
        return GetSelectQueryStatic(uid);
    }
}
