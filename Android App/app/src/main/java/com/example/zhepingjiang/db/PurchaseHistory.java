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
 * Business Object for Purchase History.
 */
@Data
@AllArgsConstructor
public class PurchaseHistory implements putsDBEntry {
    
    private static int COLUMN_COUNT = 10;

    private int uid;
    private StdNames stdName;
    private Vendors vendor;
    private Brands brand;
    private int contentQuantity;
    private ContentUnits contentUnit;
    private boolean isPackaged;
    private PackageUnits packageUnit;
    private String purchaseDate;
    private String expiryDate;

    public PurchaseHistory(final StdNames _stdName) {
        uid = 0;
        stdName = _stdName;
        // Assign default values to rest of fields. Set them via setters.
        vendor = new Vendors();
        brand = new Brands();
        contentQuantity = 0;
        contentUnit = new ContentUnits();
        isPackaged = false;
        packageUnit = new PackageUnits();
        purchaseDate = "2019-01-01";
        expiryDate = "2019-01-02";
    }

    public PurchaseHistory(final StdNames _stdName, final Vendors _vendor, final Brands _brand,
                           final int _contentQuantity, final ContentUnits _contentUnit, final boolean _isPackaged,
                           final PackageUnits _packageUnits, final String _purchaseDate, final String _expiryDate) {
        uid = 0;
        stdName = _stdName;
        vendor = _vendor;
        brand = _brand;
        contentQuantity = _contentQuantity;
        contentUnit = _contentUnit;
        isPackaged = _isPackaged;
        packageUnit = _packageUnits;
        purchaseDate = _purchaseDate;
        expiryDate = _expiryDate;
    }


    public static String GetTableNameStatic() {
        return "purchase_history";
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

    public static Set<PurchaseHistory> FromHTMLTableStr(final String htmlTableStr) {
        final Set<PurchaseHistory> parsedObjs = Sets.newHashSet();
        final Document htmlDoc = Jsoup.parse(htmlTableStr);

        final Elements htmlTables = htmlDoc.select("table");
        htmlTables.stream().filter(PurchaseHistory::isValidTableSchema).forEach(htmlTable -> {
            htmlTable.select("tr").stream().forEach(tableRow -> {
                ArrayList<String> columns = Lists.newArrayList(tableRow.select("td").stream()
                        .sequential().map(Element::text).collect(Collectors.toList()));

                if (columns.size() != COLUMN_COUNT) return;

                final int uid = Integer.parseInt(columns.get(0));
                final StdNames stdName = new StdNames(columns.get(1));
                final Vendors vendor = new Vendors(columns.get(2));
                final Brands brand = new Brands(columns.get(3));
                final int contentQuantity = Integer.parseInt(columns.get(4));
                final ContentUnits contentUnit = new ContentUnits(columns.get(5));
                final boolean isPackaged = "T".equals(columns.get(6));
                final PackageUnits packageUnit = new PackageUnits(columns.get(7));
                final String purchaseDate = columns.get(8);
                final String expiryDate = columns.get(9);

                final PurchaseHistory convertedObj = new PurchaseHistory(uid, stdName, vendor, brand,
                        contentQuantity, contentUnit, isPackaged, packageUnit, purchaseDate, expiryDate);

                parsedObjs.add(convertedObj);
            });
        });

        return parsedObjs;
    }

    @VisibleForTesting
    public static boolean isValidTableSchema (final Element htmlTable) {
        final Set<String> expectedColumns = Sets.newHashSet("uid", "std_name", "vendor", "brand", "content_quantity",
                "content_unit", "is_packaged", "package_unit", "purchase_date", "expiry_date");
        final Set<String> actualColumns = htmlTable.select("th").stream()
                .map(Element::text).collect(Collectors.toSet());

        return actualColumns.equals(expectedColumns);
    }

    public String getTableName() {
        return GetTableNameStatic();
    }

    public String getPartialFieldsStr() { return "(std_name, vendor, brand, content_quantity, content_unit, is_packaged, package_unit, purchase_date, expiry_date)"; }

    public String getInsertQuery() {
        return stdName.getUpsertQuery() +
                vendor.getUpsertQuery() +
                brand.getUpsertQuery() +
                contentUnit.getUpsertQuery() +
                packageUnit.getUpsertQuery() +
                "insert into " + getTableName() + " " + getPartialFieldsStr() + " values ('" + stdName.getStdName() + "','" +
                    vendor.getVendorName() + "','" + brand.getBrandName() + "'," + contentQuantity + ",'" +
                    contentUnit.getUnit() + "','" + (isPackaged ? 'T' : 'F') + "','" + packageUnit.getUnit() + "','" + purchaseDate + "','" + expiryDate +  "');";
    }

    public String getUpsertQuery() {
        return stdName.getUpsertQuery() +
                vendor.getUpsertQuery() +
                brand.getUpsertQuery() +
                contentUnit.getUpsertQuery() +
                packageUnit.getUpsertQuery() +
                "insert ignore into " + getTableName() + " " + getPartialFieldsStr() + " values ('" + stdName.getStdName() + "','" +
                    vendor.getVendorName() + "','" + brand.getBrandName() + "'," + contentQuantity + ",'" +
                    contentUnit.getUnit() + "','" + (isPackaged ? 'T' : 'F') + "','" + packageUnit.getUnit() + "','" + purchaseDate + "','" + expiryDate +  "');";
    }

    public String getDeleteQuery() {
        return GetDeleteQueryStatic(uid);
    }

    public String getSelectQuery() {
        return GetSelectQueryStatic(uid);
    }
}
