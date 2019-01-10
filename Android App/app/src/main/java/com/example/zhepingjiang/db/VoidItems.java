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
 * Business Object for Void Items.
 */
@Data
@AllArgsConstructor
public class VoidItems implements putsDBEntry {

    private static int COLUMN_COUNT = 4;
    
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

    public static Set<VoidItems> FromHTMLTableStr(final String htmlTableStr) {
        final Set<VoidItems> parsedObjs = Sets.newHashSet();
        final Document htmlDoc = Jsoup.parse(htmlTableStr);

        final Elements htmlTables = htmlDoc.select("table");
        htmlTables.stream().filter(VoidItems::isValidTableSchema).forEach(htmlTable -> {
            htmlTable.select("tr").stream().forEach(tableRow -> {
                ArrayList<String> columns = Lists.newArrayList(tableRow.select("td").stream()
                        .sequential().map(Element::text).collect(Collectors.toList()));

                if (columns.size() != COLUMN_COUNT) return;

                final int uid = Integer.parseInt(columns.get(0));
                final StdNames stdName = new StdNames(columns.get(1));
                final Statuses voidStatus = new Statuses(columns.get(2));
                final String timeStamp = columns.get(3);

                final VoidItems convertedObj = new VoidItems(uid, stdName, voidStatus, timeStamp);

                parsedObjs.add(convertedObj);
            });
        });

        return parsedObjs;
    }

    @VisibleForTesting
    public static boolean isValidTableSchema (final Element htmlTable) {
        final Set<String> expectedColumns = Sets.newHashSet("uid", "std_name", "void_status", "time_stamp");
        final Set<String> actualColumns = htmlTable.select("th").stream()
                .map(Element::text).collect(Collectors.toSet());

        return actualColumns.equals(expectedColumns);
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
