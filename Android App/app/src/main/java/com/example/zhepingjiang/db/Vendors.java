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
 * Business object for Content Units.
 */
@Data
@AllArgsConstructor
public class Vendors implements putsDBEntry {

    private static String DEFAULT_VENDOR = "default";
    
    private static int COLUMN_COUNT = 1;

    private String vendorName;

    public Vendors() {
        vendorName = DEFAULT_VENDOR;
    }


    public static String GetTableNameStatic() {
        return "vendors";
    }

    public static String GetDeleteQueryStatic(final String vendorName) {
        return "delete from " + GetTableNameStatic() + " where vendor_name = '" + vendorName + "';";
    }

    public static String GetSelectQueryStatic(final String vendorName) {
        return "select * from " + GetTableNameStatic() + " where vendor_name = '" + vendorName + "';";
    }

    public static String GetSelectAllQueryStatic() {
        return "select * from " + GetTableNameStatic() + ";";
    }

    public static Set<Vendors> FromHTMLTableStr(final String htmlTableStr) {
        final Set<Vendors> parsedObjs = Sets.newHashSet();
        final Document htmlDoc = Jsoup.parse(htmlTableStr);

        final Elements htmlTables = htmlDoc.select("table");
        htmlTables.stream().filter(Vendors::isValidTableSchema).forEach(htmlTable -> {
            htmlTable.select("tr").stream().forEach(tableRow -> {
                ArrayList<String> columns = Lists.newArrayList(tableRow.select("td").stream()
                        .sequential().map(Element::text).collect(Collectors.toList()));

                if (columns.size() != COLUMN_COUNT) return;

                final String vendorName = columns.get(0);

                final Vendors convertedObj = new Vendors(vendorName);

                parsedObjs.add(convertedObj);
            });
        });

        return parsedObjs;
    }

    @VisibleForTesting
    public static boolean isValidTableSchema (final Element htmlTable) {
        final Set<String> expectedColumns = Sets.newHashSet("vendor_name");
        final Set<String> actualColumns = htmlTable.select("th").stream()
                .map(Element::text).collect(Collectors.toSet());

        return actualColumns.equals(expectedColumns);
    }

    public String getTableName() {
        return GetTableNameStatic();
    }

    public String getInsertQuery() {
        return "insert into " + getTableName() + " values ('" + vendorName + "');";
    }

    public String getUpsertQuery() {
        return "insert ignore into " + getTableName() + " values ('" + vendorName + "');";
    }

    public String getDeleteQuery() {
        return GetDeleteQueryStatic(vendorName);
    }

    public String getSelectQuery() {
        return GetSelectQueryStatic(vendorName);
    }
}
