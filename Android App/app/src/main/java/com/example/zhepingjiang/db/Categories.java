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
 * Business object for Categories
 */
@Data
@AllArgsConstructor
public class Categories implements putsDBEntry {

    private static String DEFAULT_CATEGORY = "uncategorized";
    
    private static int COLUMN_COUNT = 1;

    private String category;

    public Categories() {
        category = DEFAULT_CATEGORY;
    }


    public static String GetTableNameStatic() {
        return "categories";
    }

    public static String GetDeleteQueryStatic(final String category) {
        return "delete from " + GetTableNameStatic() + " where category = '" + category + "';";
    }

    public static String GetSelectQueryStatic(final String category) {
        return "select * from " + GetTableNameStatic() + " where category = '" + category + "';";
    }

    public static String GetSelectAllQueryStatic() {
        return "select * from " + GetTableNameStatic() + ";";
    }

    public static Set<Categories> FromHTMLTableStr(final String htmlTableStr) {
        final Set<Categories> parsedObjs = Sets.newHashSet();
        final Document htmlDoc = Jsoup.parse(htmlTableStr);

        final Elements htmlTables = htmlDoc.select("table");
        htmlTables.stream().filter(Categories::isValidTableSchema).forEach(htmlTable -> {
            htmlTable.select("tr").stream().forEach(tableRow -> {
                ArrayList<String> columns = Lists.newArrayList(tableRow.select("td").stream()
                        .sequential().map(Element::text).collect(Collectors.toList()));

                if (columns.size() != COLUMN_COUNT) return;

                final String category = columns.get(0);

                final Categories convertedObj = new Categories(category);

                parsedObjs.add(convertedObj);
            });
        });

        return parsedObjs;
    }

    @VisibleForTesting
    public static boolean isValidTableSchema (final Element htmlTable) {
        final Set<String> expectedColumns = Sets.newHashSet("category");
        final Set<String> actualColumns = htmlTable.select("th").stream()
                .map(Element::text).collect(Collectors.toSet());

        return actualColumns.equals(expectedColumns);
    }

    public String getTableName() {
        return GetTableNameStatic();
    }

    public String getInsertQuery() {
        return "insert into " + getTableName() + " values ('" + category + "');";
    }

    public String getUpsertQuery() {
        return "insert ignore into " + getTableName() + " values ('" + category + "');";
    }

    public String getDeleteQuery() {
        return GetDeleteQueryStatic(category);
    }

    public String getSelectQuery() {
        return GetSelectQueryStatic(category);
    }
}
