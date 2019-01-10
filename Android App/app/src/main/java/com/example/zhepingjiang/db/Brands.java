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
 * Business object for Brands
 */
@Data
@AllArgsConstructor
public class Brands implements putsDBEntry {

    private static String DEFAULT_BRAND = "default";

    private static int COLUMN_COUNT = 1;

    private String brandName;

    public Brands() {
        brandName = DEFAULT_BRAND;
    }


    public static String GetTableNameStatic() {
        return "brands";
    }

    public static String GetDeleteQueryStatic(final String brandName) {
        return "delete from " + GetTableNameStatic() + " where brand_name = '" + brandName + "';";
    }

    public static String GetSelectQueryStatic(final String brandName) {
        return "select * from " + GetTableNameStatic() + " where brand_name = '" + brandName + "';";
    }

    public static String GetSelectAllQueryStatic() {
        return "select * from " + GetTableNameStatic() + ";";
    }

    public static Set<Brands> FromHTMLTableStr(final String htmlTableStr) {
        final Set<Brands> parsedObjs = Sets.newHashSet();
        final Document htmlDoc = Jsoup.parse(htmlTableStr);

        final Elements htmlTables = htmlDoc.select("table");
        htmlTables.stream().filter(Brands::isValidTableSchema).forEach(htmlTable -> {
            htmlTable.select("tr").stream().forEach(tableRow -> {
                ArrayList<String> columns = Lists.newArrayList(tableRow.select("td").stream()
                        .sequential().map(Element::text).collect(Collectors.toList()));

                if (columns.size() != COLUMN_COUNT) return;

                final String brandName = columns.get(0);

                final Brands convertedObj = new Brands(brandName);

                parsedObjs.add(convertedObj);
            });
        });

        return parsedObjs;
    }

    @VisibleForTesting
    public static boolean isValidTableSchema (final Element htmlTable) {
        final Set<String> expectedColumns = Sets.newHashSet("brand_name");
        final Set<String> actualColumns = htmlTable.select("th").stream()
                .map(Element::text).collect(Collectors.toSet());

        return actualColumns.equals(expectedColumns);
    }

    public String getTableName() {
        return GetTableNameStatic();
    }

    public String getInsertQuery() {
        return "insert into " + getTableName() + " values ('" + brandName + "');";
    }

    public String getUpsertQuery() {
        return "insert ignore into " + getTableName() + " values ('" + brandName + "');";
    }

    public String getDeleteQuery() {
        return GetDeleteQueryStatic(brandName);
    }

    public String getSelectQuery() {
        return GetSelectQueryStatic(brandName);
    }
}
