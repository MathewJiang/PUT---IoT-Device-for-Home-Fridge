package com.example.zhepingjiang.db;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Business object for Actions
 */
@Data
@AllArgsConstructor
public class Actions implements putsDBEntry {

    private static String DEFAULT_USER_ACTION = "default";
    private static int COLUMN_COUNT = 1;

    private String userAction;

    public Actions() {
        userAction = DEFAULT_USER_ACTION;
    }


    public static String GetTableNameStatic() {
        return "actions";
    }

    public static String GetDeleteQueryStatic(final String userAction) {
        return "delete from " + GetTableNameStatic() + " where user_action = '" + userAction + "';";
    }

    public static String GetSelectQueryStatic(final String userAction) {
        return "select * from " + GetTableNameStatic() + " where user_action = '" + userAction + "';";
    }

    public static String GetSelectAllQueryStatic() {
        return "select * from " + GetTableNameStatic() + ";";
    }

    public static Set<Actions> FromHTMLTableStr(final String htmlTableStr) {
        final Set<Actions> parsedObjs = Sets.newHashSet();
        final Document htmlDoc = Jsoup.parse(htmlTableStr);

        final Elements htmlTables = htmlDoc.select("table");
        htmlTables.stream().filter(Actions::isValidTableSchema).forEach(htmlTable -> {
            htmlTable.select("tr").stream().forEach(tableRow -> {
                ArrayList<String> columns = Lists.newArrayList(tableRow.select("td").stream()
                        .sequential().map(Element::text).collect(Collectors.toList()));

                if (columns.size() != COLUMN_COUNT) return;

                final String userAction = columns.get(0);

                final Actions action = new Actions(userAction);

                parsedObjs.add(action);
            });
        });

        return parsedObjs;
    }

    @VisibleForTesting
    public static boolean isValidTableSchema (final Element htmlTable) {
        final Set<String> expectedColumns = Sets.newHashSet("user_action");
        final Set<String> actualColumns = htmlTable.select("th").stream()
                .map(Element::text).collect(Collectors.toSet());

        return actualColumns.equals(expectedColumns);
    }

    public String getTableName() {
        return GetTableNameStatic();
    }

    public String getInsertQuery() {
        return "insert into " + getTableName() + " values ('" + userAction + "');";
    }

    public String getUpsertQuery() {
        return "insert ignore into " + getTableName() + " values ('" + userAction + "');";
    }

    public String getDeleteQuery() {
        return GetDeleteQueryStatic(userAction);
    }

    public String getSelectQuery() {
        return GetSelectQueryStatic(userAction);
    }
}
