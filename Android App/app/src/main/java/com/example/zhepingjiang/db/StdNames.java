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
 * Business Object for Std_Name.
 */
@Data
@AllArgsConstructor
public class StdNames implements putsDBEntry {

    private static int COLUMN_COUNT = 2;
    
    private String stdName;
    private Categories category;

    public StdNames(final String _stdName) {
        stdName = _stdName;
        category = new Categories();
    }


    public static String GetTableNameStatic() {
        return "std_names";
    }

    public static String GetDeleteQueryStatic(final String stdName) {
        return "delete from " + GetTableNameStatic() + " where std_name = '" + stdName + "';";
    }

    public static String GetSelectQueryStatic(final String stdName) {
        return "select * from " + GetTableNameStatic() + " where std_name = '" + stdName + "';";
    }

    public static String GetSelectAllQueryStatic() {
        return "select * from " + GetTableNameStatic() + ";";
    }

    public static Set<StdNames> FromHTMLTableStr(final String htmlTableStr) {
        final Set<StdNames> parsedObjs = Sets.newHashSet();
        final Document htmlDoc = Jsoup.parse(htmlTableStr);

        final Elements htmlTables = htmlDoc.select("table");
        htmlTables.stream().filter(StdNames::isValidTableSchema).forEach(htmlTable -> {
            htmlTable.select("tr").stream().forEach(tableRow -> {
                ArrayList<String> columns = Lists.newArrayList(tableRow.select("td").stream()
                        .sequential().map(Element::text).collect(Collectors.toList()));

                if (columns.size() != COLUMN_COUNT) return;

                final String std_name = columns.get(0);
                final Categories category = new Categories(columns.get(1));

                final StdNames convertedObj = new StdNames(std_name, category);

                parsedObjs.add(convertedObj);
            });
        });

        return parsedObjs;
    }

    @VisibleForTesting
    public static boolean isValidTableSchema (final Element htmlTable) {
        final Set<String> expectedColumns = Sets.newHashSet("std_name", "category");
        final Set<String> actualColumns = htmlTable.select("th").stream()
                .map(Element::text).collect(Collectors.toSet());

        return actualColumns.equals(expectedColumns);
    }

    public String getTableName() {
        return GetTableNameStatic();
    }

    public String getInsertQuery() {
        return category.getUpsertQuery() +
                "insert into " + getTableName() + " values ('" + stdName + "','" + category.getCategory() + "');";
    }

    public String getUpsertQuery() {
        return category.getUpsertQuery() +
                "insert ignore into " + getTableName() + " values ('" + stdName + "','" + category.getCategory() + "');";
    }

    public String getDeleteQuery() {
        return GetDeleteQueryStatic(stdName);
    }

    public String getSelectQuery() {
        return GetSelectQueryStatic(stdName);
    }


}
