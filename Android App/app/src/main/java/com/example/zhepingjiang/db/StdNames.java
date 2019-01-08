package com.example.zhepingjiang.db;

import lombok.AllArgsConstructor;
import lombok.Data;

/*
 * Business Object for Std_Name.
 */
@Data
@AllArgsConstructor
public class StdNames implements putsDBEntry {

    private String stdName;
    private Categories category;

    public StdNames(final String _stdName) {
        stdName = _stdName;
        category = new Categories();
    }


    public static String GetTableNameStatic() {
        return "std_names";
    }

    public static String GetDeleteQueryStatic(final String stdName) {
        return "delete from " + GetTableNameStatic() + " where std_name = '" + stdName + "';";
    }

    public static String GetSelectQueryStatic(final String stdName) {
        return "select * from " + GetTableNameStatic() + " where std_name = '" + stdName + "';";
    }

    public static String GetSelectAllQueryStatic() {
        return "select * from " + GetTableNameStatic() + ";";
    }

    public String getTableName() {
        return GetTableNameStatic();
    }

    public String getInsertQuery() {
        return category.getUpsertQuery() +
                "insert into " + getTableName() + " values ('" + stdName + "','" + category.getCategory() + "');";
    }

    public String getUpsertQuery() {
        return category.getUpsertQuery() +
                "insert ignore into " + getTableName() + " values ('" + stdName + "','" + category.getCategory() + "');";
    }

    public String getDeleteQuery() {
        return GetDeleteQueryStatic(stdName);
    }

    public String getSelectQuery() {
        return GetSelectQueryStatic(stdName);
    }


}
