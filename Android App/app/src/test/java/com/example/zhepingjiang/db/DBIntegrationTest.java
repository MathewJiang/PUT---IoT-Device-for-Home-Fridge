package com.example.zhepingjiang.db;

import org.junit.Test;
import org.junit.Ignore;

// DB integration tests
public class DBIntegrationTest {

    private void printCaveat() {
        System.out.println("\n\n****Please run following query(s) in mySQL DB, remember to replace any serial ids with actual ones:****");
    }

    @Ignore
    public void testInsertGroceryRecord() {
        final StdNames stdName = new StdNames("Orange juice", new Categories("juice"));
        final Vendors vendor = new Vendors("Loblaws");
        final Brands brand = new Brands("Dole");
        final int contentQuantity = 1000;
        final char isPackaged = 'T';
        final ContentUnits contentUnit = new ContentUnits("ml");
        final PackageUnits packageUnit = new PackageUnits("box");
        final String purchaseDate = "2019-01-07";
        final String expiryDate = "2019-02-03";

        final String lastUpdatedTimeStamp = "2019-01-07 19:43:52";
        final Statuses status = new Statuses("unopened");

        final int consumedQuantity = 800;
        final int remainingQuantity = 200;
        final Actions action = new Actions("consumption");
        final String consumptionTimeStamp = "2019-01-07 21:23:45";


        final String phQuery = new PurchaseHistory(stdName, vendor, brand, contentQuantity, contentUnit,
                isPackaged, packageUnit, purchaseDate, expiryDate).getInsertQuery();
        final String gsQuery = new GroceryStorage(8, stdName, contentQuantity, contentUnit,
                lastUpdatedTimeStamp, purchaseDate, expiryDate, status).getInsertQuery();
        final String csQuery = new ConsumptionHistory(8, stdName, consumedQuantity,
                remainingQuantity, action, consumptionTimeStamp).getInsertQuery();

        printCaveat();
        System.out.println("purchase query:");
        System.out.println(DBAccess.GetFullQuery(phQuery));
        System.out.println("storage query:");
        System.out.println(DBAccess.GetFullQuery(gsQuery));
        System.out.println("consumption query:");
        System.out.println(DBAccess.GetFullQuery(csQuery));

    }

    @Ignore
    public void testInsertBarcode() {
        final StdNames stdName = new StdNames("Orange juice", new Categories("juice"));
        final String barcode = "01234567";
        final Brands brand = new Brands("Dole");
        final int contentQuantity = 1000;
        final char isPackaged = 'T';
        final ContentUnits contentUnit = new ContentUnits("ml");
        final PackageUnits packageUnit = new PackageUnits("box");

        final String bcQuery = new Barcodes(barcode, brand, stdName, contentQuantity, contentUnit,
                isPackaged, packageUnit).getInsertQuery();

        printCaveat();
        System.out.println("barcode query:");
        System.out.println(DBAccess.GetFullQuery(bcQuery));
    }

    @Test
    public void testInsertTemperatureData() {
        final String tdQuery = new TemperatureData("2019-01-09 19:20:21", -3.5).getInsertQuery();

        printCaveat();
        System.out.println("temperature data query:");
        System.out.println(DBAccess.GetFullQuery(tdQuery));
    }
}
