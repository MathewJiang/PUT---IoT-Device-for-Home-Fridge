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
 * Business object for Content Units
 */
@Data
@AllArgsConstructor
public class PackageUnits implements putsDBEntry {

    private static String DEFAULT_PACKAGE_UNIT = "default";

    private static int COLUMN_COUNT = 1;

    private String unit;

    public PackageUnits() {
        unit = DEFAULT_PACKAGE_UNIT;
    }


    public static String GetTableNameStatic() {
        return "package_units";
    }

    public static String GetDeleteQueryStatic(final String packageUnit) {
        return "delete from " + GetTableNameStatic() + " where unit = '" + packageUnit + "';";
    }

    public static String GetSelectQueryStatic(final String packageUnit) {
        return "select * from " + GetTableNameStatic() + " where unit = '" + packageUnit + "';";
    }

    public static String GetSelectAllQueryStatic() {
        return "select * from " + GetTableNameStatic() + ";";
    }

    public static Set<PackageUnits> FromHTMLTableStr(final String htmlTableStr) {
        final Set<PackageUnits> parsedObjs = Sets.newHashSet();
        final Document htmlDoc = Jsoup.parse(htmlTableStr);

        final Elements htmlTables = htmlDoc.select("table");
        htmlTables.stream().filter(PackageUnits::isValidTableSchema).forEach(htmlTable -> {
            htmlTable.select("tr").stream().forEach(tableRow -> {
                ArrayList<String> columns = Lists.newArrayList(tableRow.select("td").stream()
                        .sequential().map(Element::text).collect(Collectors.toList()));

                if (columns.size() != COLUMN_COUNT) return;

                final String unit = columns.get(0);

                final PackageUnits convertedObj = new PackageUnits(unit);

                parsedObjs.add(convertedObj);
            });
        });

        return parsedObjs;
    }

    @VisibleForTesting
    public static boolean isValidTableSchema (final Element htmlTable) {
        final Set<String> expectedColumns = Sets.newHashSet("unit");
        final Set<String> actualColumns = htmlTable.select("th").stream()
                .map(Element::text).collect(Collectors.toSet());

        return actualColumns.equals(expectedColumns);
    }

    public String getTableName() {
        return GetTableNameStatic();
    }

    public String getInsertQuery() {
        return "insert into " + getTableName() + " values ('" + unit + "');";
    }

    public String getUpsertQuery() {
        return "insert ignore into " + getTableName() + " values ('" + unit + "');";
    }

    public String getDeleteQuery() {
        return GetDeleteQueryStatic(unit);
    }

    public String getSelectQuery() {
        return GetSelectQueryStatic(unit);
    }
}
