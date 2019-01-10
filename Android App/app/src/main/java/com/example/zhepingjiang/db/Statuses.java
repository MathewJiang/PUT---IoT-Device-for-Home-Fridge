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

/*
 * Business object for Barcodes
 */
@Data
@AllArgsConstructor
public class Statuses implements putsDBEntry {

    private static String DEFAULT_STATUS = "default";
    
    private static int COLUMN_COUNT = 1;

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

    public static Set<Statuses> FromHTMLTableStr(final String htmlTableStr) {
        final Set<Statuses> parsedObjs = Sets.newHashSet();
        final Document htmlDoc = Jsoup.parse(htmlTableStr);

        final Elements htmlTables = htmlDoc.select("table");
        htmlTables.stream().filter(Statuses::isValidTableSchema).forEach(htmlTable -> {
            htmlTable.select("tr").stream().forEach(tableRow -> {
                ArrayList<String> columns = Lists.newArrayList(tableRow.select("td").stream()
                        .sequential().map(Element::text).collect(Collectors.toList()));

                if (columns.size() != COLUMN_COUNT) return;

                final String status = columns.get(0);

                final Statuses convertedObj = new Statuses(status);

                parsedObjs.add(convertedObj);
            });
        });

        return parsedObjs;
    }

    @VisibleForTesting
    public static boolean isValidTableSchema (final Element htmlTable) {
        final Set<String> expectedColumns = Sets.newHashSet("status");
        final Set<String> actualColumns = htmlTable.select("th").stream()
                .map(Element::text).collect(Collectors.toSet());

        return actualColumns.equals(expectedColumns);
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
