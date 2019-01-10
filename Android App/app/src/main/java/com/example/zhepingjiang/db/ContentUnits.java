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
public class ContentUnits implements putsDBEntry {

    public static String DEFAULT_CONTENT_UNIT = "default";

    public static int COLUMN_COUNT = 1;

    private String unit;

    public ContentUnits() {
        unit = DEFAULT_CONTENT_UNIT;
    }


    public static String GetTableNameStatic() {
        return "content_units";
    }

    public static String GetDeleteQueryStatic(final String contentUnit) {
        return "delete from " + GetTableNameStatic() + " where unit = '" + contentUnit + "';";
    }

    public static String GetSelectQueryStatic(final String contentUnit) {
        return "select * from " + GetTableNameStatic() + " where unit = '" + contentUnit + "';";
    }

    public static String GetSelectAllQueryStatic() {
        return "select * from " + GetTableNameStatic() + ";";
    }

    public static Set<ContentUnits> FromHTMLTableStr(final String htmlTableStr) {
        final Set<ContentUnits> parsedObjs = Sets.newHashSet();
        final Document htmlDoc = Jsoup.parse(htmlTableStr);

        final Elements htmlTables = htmlDoc.select("table");
        htmlTables.stream().filter(ContentUnits::isValidTableSchema).forEach(htmlTable -> {
            htmlTable.select("tr").stream().forEach(tableRow -> {
                ArrayList<String> columns = Lists.newArrayList(tableRow.select("td").stream()
                        .sequential().map(Element::text).collect(Collectors.toList()));

                if (columns.size() != COLUMN_COUNT) return;

                final String unit = columns.get(0);

                final ContentUnits convertedObj = new ContentUnits(unit);

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