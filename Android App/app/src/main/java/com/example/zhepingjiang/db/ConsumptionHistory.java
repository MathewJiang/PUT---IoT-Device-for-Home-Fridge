package com.example.zhepingjiang.db;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Business Object for Consumption History records
 */
@Data
@AllArgsConstructor
public class ConsumptionHistory implements putsDBEntry {

    private static int COLUMN_COUNT = 7;

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

    public static Set<ConsumptionHistory> FromHTMLTableStr(final String htmlTableStr) {
        final Set<ConsumptionHistory> parsedObjs = Sets.newHashSet();
        final Document htmlDoc = Jsoup.parse(htmlTableStr);

        final Elements htmlTables = htmlDoc.select("table");
        htmlTables.stream().filter(ConsumptionHistory::isValidTableSchema).forEach(htmlTable -> {
            htmlTable.select("tr").stream().forEach(tableRow -> {
                ArrayList<String> columns = Lists.newArrayList(tableRow.select("td").stream()
                        .sequential().map(Element::text).collect(Collectors.toList()));

                if (columns.size() != COLUMN_COUNT) return;

                final int opid = Integer.parseInt(columns.get(0));
                final int uid = Integer.parseInt(columns.get(1));
                final StdNames stdName = new StdNames(columns.get(2));
                final int consumedQuantity = Integer.parseInt(columns.get(3));
                final int remainingQuantity = Integer.parseInt(columns.get(4));
                final Actions action = new Actions(columns.get(5));
                final String timeStamp = columns.get(6);

                final ConsumptionHistory convertedObj = new ConsumptionHistory(opid, uid, stdName,
                        consumedQuantity, remainingQuantity, action, timeStamp);

                parsedObjs.add(convertedObj);
            });
        });

        return parsedObjs;
    }

    @VisibleForTesting
    public static boolean isValidTableSchema (final Element htmlTable) {
        final Set<String> expectedColumns = Sets.newHashSet("opid", "uid", "std_name", "consumed_quantity",
                "remaining_quantity", "action", "time_stamp");
        final Set<String> actualColumns = htmlTable.select("th").stream()
                .map(Element::text).collect(Collectors.toSet());

        return actualColumns.equals(expectedColumns);
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
