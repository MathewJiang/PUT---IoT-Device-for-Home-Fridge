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
 * Business Object for Grocery Storage.
 */
@Data
@AllArgsConstructor
public class GroceryStorage {
    
    private static int COLUMN_COUNT = 8;

    private int uid;
    private StdNames stdName;
    private int contentQuantity;
    private ContentUnits contentUnit;
    private String lastUpdatedTimeStamp;
    private String purchaseDate;
    private String expiryDate;
    private Statuses status;

    // Constructor that builds a new storage entry from a purchase entry.
    public GroceryStorage(final PurchaseHistory ph, final Statuses _status, final String _lastUpdatedTimeStamp) {
        uid = ph.getUid();
        stdName = ph.getStdName();
        contentQuantity = ph.getContentQuantity();
        contentUnit = ph.getContentUnit();
        lastUpdatedTimeStamp = _lastUpdatedTimeStamp;
        purchaseDate = ph.getPurchaseDate();
        expiryDate = ph.getExpiryDate();
        status = _status;
    }

    public static String GetTableNameStatic() {
        return "grocery_storage";
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

    public static Set<GroceryStorage> FromHTMLTableStr(final String htmlTableStr) {
        final Set<GroceryStorage> parsedObjs = Sets.newHashSet();
        final Document htmlDoc = Jsoup.parse(htmlTableStr);

        final Elements htmlTables = htmlDoc.select("table");
        htmlTables.stream().filter(GroceryStorage::isValidTableSchema).forEach(htmlTable -> {
            htmlTable.select("tr").stream().forEach(tableRow -> {
                ArrayList<String> columns = Lists.newArrayList(tableRow.select("td").stream()
                        .sequential().map(Element::text).collect(Collectors.toList()));

                if (columns.size() != COLUMN_COUNT) return;

                final int uid = Integer.parseInt(columns.get(0));
                final StdNames stdName = new StdNames(columns.get(1));
                final int contentQuantity = Integer.parseInt(columns.get(2));
                final ContentUnits contentUnit = new ContentUnits(columns.get(3));
                final String lastUpdatedTimeStamp = columns.get(4);
                final String purchaseDate = columns.get(5);
                final String expiryDate = columns.get(6);
                final Statuses status = new Statuses(columns.get(7));

                final GroceryStorage convertedObj = new GroceryStorage(uid, stdName, contentQuantity,
                        contentUnit, lastUpdatedTimeStamp, purchaseDate, expiryDate, status);

                parsedObjs.add(convertedObj);
            });
        });

        return parsedObjs;
    }

    @VisibleForTesting
    public static boolean isValidTableSchema (final Element htmlTable) {
        final Set<String> expectedColumns = Sets.newHashSet("uid", "std_name", "content_quantity",
                "content_unit", "last_updated", "purchase_date", "expiry_date", "status");
        final Set<String> actualColumns = htmlTable.select("th").stream()
                .map(Element::text).collect(Collectors.toSet());

        return actualColumns.equals(expectedColumns);
    }

    public String getTableName() {
        return GetTableNameStatic();
    }

    public String getInsertQuery() {
        return stdName.getUpsertQuery() +
                contentUnit.getUpsertQuery() +
                status.getUpsertQuery() +
                "insert into " + getTableName() + " values (" + ( uid == 0 ? "LAST_INSERT_ID()" : uid) + ",'" + stdName.getStdName() + "'," +
                    contentQuantity + ",'" + contentUnit.getUnit() + "','" + lastUpdatedTimeStamp + "','" +
                    purchaseDate + "','" + expiryDate + "','" + status.getStatus() +  "');";
    }

    public String getUpsertQuery() {
        return stdName.getUpsertQuery() +
                contentUnit.getUpsertQuery() +
                status.getUpsertQuery() +
                "insert ignore into " + getTableName() + " values (" + ( uid == 0 ? "LAST_INSERT_ID()" : uid) + ",'" + stdName.getStdName() + "'," +
                contentQuantity + ",'" + contentUnit.getUnit() + "','" + lastUpdatedTimeStamp + "','" +
                purchaseDate + "','" + expiryDate + "','" + status.getStatus() +  "');";
    }

    public String getDeleteQuery() {
        return GetDeleteQueryStatic(uid);
    }

    public String getSelectQuery() {
        return GetSelectQueryStatic(uid);
    }

}
