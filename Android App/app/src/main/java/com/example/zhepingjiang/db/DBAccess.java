package com.example.zhepingjiang.db;

/**
 * Util functions to access putsDB.
 */
public class DBAccess {

    public static int DUMMY_VALUE = 1;

    //static String ROOT_URL = "http://ece496puts.ddns.net:59496/raw_sql/";
    static String USE_DB_COMMAND = "use putsDB;";

    public static String GetFullQuery(final String query) {
        return USE_DB_COMMAND + query;
    }

    public static String GetLastInsertIdQuery() {
        return GetFullQuery("select LAST_INSERT_ID();");
    }
}
