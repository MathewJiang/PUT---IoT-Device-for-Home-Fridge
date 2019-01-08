package com.example.zhepingjiang.db;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Business object for Actions
 */
@Data
@AllArgsConstructor
public class Actions implements putsDBEntry {

    private static String DEFAULT_USER_ACTION = "default";

    private String userAction;

    public Actions() {
        userAction = DEFAULT_USER_ACTION;
    }


    public static String GetTableNameStatic() {
        return "actions";
    }

    public static String GetDeleteQueryStatic(final String userAction) {
        return "delete from " + GetTableNameStatic() + " where user_action = '" + userAction + "';";
    }

    public static String GetSelectQueryStatic(final String userAction) {
        return "select * from " + GetTableNameStatic() + " where user_action = '" + userAction + "';";
    }

    public static String GetSelectAllQueryStatic() {
        return "select * from " + GetTableNameStatic() + ";";
    }

    public String getTableName() {
        return GetTableNameStatic();
    }

    public String getInsertQuery() {
        return "insert into " + getTableName() + " values ('" + userAction + "');";
    }

    public String getUpsertQuery() {
        return "insert ignore into " + getTableName() + " values ('" + userAction + "');";
    }

    public String getDeleteQuery() {
        return GetDeleteQueryStatic(userAction);
    }

    public String getSelectQuery() {
        return GetSelectQueryStatic(userAction);
    }
}
