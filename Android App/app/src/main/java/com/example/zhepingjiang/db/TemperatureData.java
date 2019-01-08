package com.example.zhepingjiang.db;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Business Object for Temperature Data.
 */
@Data
@AllArgsConstructor
public class TemperatureData implements putsDBEntry {

    private int rid;
    private String time;
    private double temperature;

    public TemperatureData(final String _time, final double _temperature) {
        rid = 0;
        time = _time;
        temperature = _temperature;
    }

    public static String GetTableNameStatic() {
        return "temperature_data";
    }

    public static String GetDeleteQueryStatic(final int rid) {
        return "delete from " + GetTableNameStatic() + " where rid = " + rid + ";";
    }

    public static String GetSelectQueryStatic(final int rid) {
        return "select * from " + GetTableNameStatic() + " where rid = " + rid + ";";
    }

    public static String GetSelectAllQueryStatic() {
        return "select * from " + GetTableNameStatic() + ";";
    }

    public String getTableName() {
        return GetTableNameStatic();
    }

    public String getPartialFieldsStr() { return "(time, temperature)"; }

    public String getInsertQuery() {
        return "insert into " + getTableName() + " " + getPartialFieldsStr() +  " values ('" + time + "'," + temperature + ");";
    }

    public String getUpsertQuery() {
        return "insert ignore into " + getTableName() + " " + getPartialFieldsStr() +  " values ('" + time + "'," + temperature + ");";
    }

    public String getDeleteQuery() {
        return GetDeleteQueryStatic(rid);
    }

    public String getSelectQuery() {
        return GetSelectQueryStatic(rid);
    }
}
