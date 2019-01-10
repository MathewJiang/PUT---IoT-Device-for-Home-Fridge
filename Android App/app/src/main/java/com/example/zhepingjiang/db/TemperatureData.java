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
 * Business Object for Temperature Data.
 */
@Data
@AllArgsConstructor
public class TemperatureData implements putsDBEntry {
    
    private static int COLUMN_COUNT = 3;

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

    public static Set<TemperatureData> FromHTMLTableStr(final String htmlTableStr) {
        final Set<TemperatureData> parsedObjs = Sets.newHashSet();
        final Document htmlDoc = Jsoup.parse(htmlTableStr);

        final Elements htmlTables = htmlDoc.select("table");
        htmlTables.stream().filter(TemperatureData::isValidTableSchema).forEach(htmlTable -> {
            htmlTable.select("tr").stream().forEach(tableRow -> {
                ArrayList<String> columns = Lists.newArrayList(tableRow.select("td").stream()
                        .sequential().map(Element::text).collect(Collectors.toList()));

                if (columns.size() != COLUMN_COUNT) return;

                final int rid = Integer.parseInt(columns.get(0));
                final String time = columns.get(1);
                final double temperature = Double.parseDouble(columns.get(2));

                final TemperatureData convertedObj = new TemperatureData(rid, time, temperature);

                parsedObjs.add(convertedObj);
            });
        });

        return parsedObjs;
    }

    @VisibleForTesting
    public static boolean isValidTableSchema (final Element htmlTable) {
        final Set<String> expectedColumns = Sets.newHashSet("rid", "time", "temperature");
        final Set<String> actualColumns = htmlTable.select("th").stream()
                .map(Element::text).collect(Collectors.toSet());

        return actualColumns.equals(expectedColumns);
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
