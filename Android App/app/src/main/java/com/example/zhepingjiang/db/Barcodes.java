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
 * Business object for Barcodes
 */
@Data
@AllArgsConstructor
public class Barcodes implements putsDBEntry {

    private static int COLUMN_COUNT = 7;

    private String barcode;
    private Brands brand;
    private StdNames stdName;
    private int contentQuantity;
    private ContentUnits contentUnit;
    private boolean isPackaged;
    private PackageUnits packageUnit;

    public Barcodes (final String _barcode, final StdNames _stdName) {
        barcode = _barcode;
        stdName = _stdName;
        // Default values for the rest, use setters to set them.
        brand = new Brands();
        contentQuantity = 0;
        contentUnit = new ContentUnits();
        isPackaged = true;
        packageUnit = new PackageUnits();
    }


    public static String GetTableNameStatic() {
        return "barcodes";
    }

    public static String GetDeleteQueryStatic(final String barcode) {
        return "delete from " + GetTableNameStatic() + " where barcode = '" + barcode + "';";
    }

    public static String GetSelectQueryStatic(final String barcode) {
        return "select * from " + GetTableNameStatic() + " where barcode = '" + barcode + "';";
    }

    public static String GetSelectAllQueryStatic() {
        return "select * from " + GetTableNameStatic() + ";";
    }

    public static Set<Barcodes> FromHTMLTableStr(final String htmlTableStr) {
        final Set<Barcodes> parsedObjs = Sets.newHashSet();
        final Document htmlDoc = Jsoup.parse(htmlTableStr);

        final Elements htmlTables = htmlDoc.select("table");
        htmlTables.stream().filter(Barcodes::isValidTableSchema).forEach(htmlTable -> {
            htmlTable.select("tr").stream().forEach(tableRow -> {
                ArrayList<String> columns = Lists.newArrayList(tableRow.select("td").stream()
                        .sequential().map(Element::text).collect(Collectors.toList()));

                if (columns.size() != COLUMN_COUNT) return;

                final String barcode = columns.get(0);
                final Brands brand = new Brands(columns.get(1));
                final StdNames stdName = new StdNames(columns.get(2));
                final int contentQuantity = Integer.parseInt(columns.get(3));
                final ContentUnits contentUnit = new ContentUnits(columns.get(4));
                final boolean isPackaged = "T".equals(columns.get(5));
                final PackageUnits packageUnit = new PackageUnits(columns.get(6));

                final Barcodes convertedObj = new Barcodes(barcode, brand, stdName, contentQuantity,
                        contentUnit, isPackaged, packageUnit);

                parsedObjs.add(convertedObj);
            });
        });

        return parsedObjs;
    }

    @VisibleForTesting
    public static boolean isValidTableSchema (final Element htmlTable) {
        final Set<String> expectedColumns = Sets.newHashSet("barcode", "brand", "std_name",
                "content_quantity", "content_unit", "is_packaged", "package_unit");
        final Set<String> actualColumns = htmlTable.select("th").stream()
                .map(Element::text).collect(Collectors.toSet());

        return actualColumns.equals(expectedColumns);
    }

    public String getTableName() {
        return GetTableNameStatic();
    }

    public String getInsertQuery() {
        return stdName.getUpsertQuery() +
                brand.getUpsertQuery() +
                contentUnit.getUpsertQuery() +
                packageUnit.getUpsertQuery() +
                "insert into " + getTableName() + " values ('" + barcode + "','" + brand.getBrandName() + "','" +
                    stdName.getStdName() + "'," + contentQuantity + ",'" + contentUnit.getUnit() + "','" + (isPackaged ? 'T' : 'F') + "','" + packageUnit.getUnit() + "');";
    }

    public String getUpsertQuery() {
        return stdName.getUpsertQuery() +
                brand.getUpsertQuery() +
                contentUnit.getUpsertQuery() +
                packageUnit.getUpsertQuery() +
                "insert ignore into " + getTableName() + " values ('" + barcode + "','" + brand.getBrandName() + "','" +
                stdName.getStdName() + "'," + contentQuantity + ",'" + contentUnit.getUnit() + "','" + (isPackaged ? 'T' : 'F') + "','" + packageUnit.getUnit() + "');";
    }

    public String getDeleteQuery() {
        return GetDeleteQueryStatic(barcode);
    }

    public String getSelectQuery() {
        return GetSelectQueryStatic(barcode);
    }
}