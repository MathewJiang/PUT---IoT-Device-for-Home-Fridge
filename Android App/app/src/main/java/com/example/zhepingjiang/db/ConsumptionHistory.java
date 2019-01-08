package com.example.zhepingjiang.db;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Business Object for Consumption History records
 */
@Data
@AllArgsConstructor
public class ConsumptionHistory implements putsDBEntry {

    private int opid;
    private int uid;
    private StdNames stdName;
    private int consumedQuantity;
    private int remainingQuantity;
    private Actions action;
    private String timeStamp;

    public ConsumptionHistory(final int _uid, final StdNames _stdName, final int _consumedQuantity,
                              final int _remainingQuantity, final Actions _action, final String _timeStamp) {
        opid = 0;
        uid = _uid;
        stdName = _stdName;
        consumedQuantity = _consumedQuantity;
        remainingQuantity = _remainingQuantity;
        action = _action;
        timeStamp = _timeStamp;
    }


    public static String GetTableNameStatic() {
        return "consumption_history";
    }

    public static String GetDeleteQueryStatic(final int opid) {
        return "delete from " + GetTableNameStatic() + " where opid = " + opid + ";";
    }

    public static String GetSelectQueryStatic(final int opid) {
        return "select * from " + GetTableNameStatic() + " where opid = " + opid + ";";
    }

    public static String GetSelectAllQueryStatic() {
        return "select * from " + GetTableNameStatic() + ";";
    }

    public String getTableName() {
        return GetTableNameStatic();
    }

    public String getPartialFieldsStr() { return "(uid, std_name, consumed_quantity, remaining_quantity, action, time_stamp)"; }

    public String getInsertQuery() {
        return stdName.getUpsertQuery() +
                action.getUpsertQuery() +
                "insert into " + getTableName() + " " + getPartialFieldsStr() + " values (" + uid + ",'" + stdName.getStdName() + "'," +
                    consumedQuantity + "," + remainingQuantity + ",'" + action.getUserAction() + "','" + timeStamp + "');";
    }

    public String getUpsertQuery() {
        return stdName.getUpsertQuery() +
                action.getUpsertQuery() +
                "insert ignore into " + getTableName() + " " + getPartialFieldsStr() + " values (" + uid + ",'" + stdName.getStdName() + "'," +
                    consumedQuantity + "," + remainingQuantity + ",'" + action.getUserAction() + "','" + timeStamp + "');";
    }

    public String getDeleteQuery() {
        return GetDeleteQueryStatic(opid);
    }

    public String getSelectQuery() {
        return GetSelectQueryStatic(opid);
    }
}
