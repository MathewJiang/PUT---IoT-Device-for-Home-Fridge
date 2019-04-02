package com.example.zhepingjiang.db;

/**
 * Util functions to access putsDB.
 */
public class DBAccess {

    public static int DUMMY_VALUE = 1;

    static String ROOT_URL = "http://192.168.1.120:8080/raw_sql_html/";
    static String USE_DB_COMMAND = "use putsDB;";

    public static String GetFullQuery(final String query) {
        return USE_DB_COMMAND + query;
    }

    public static String GetQueryLink(final String query) {
        return ROOT_URL + GetFullQuery(query);
    }

    public static String GetLastInsertIdQuery() {
        return GetFullQuery("select LAST_INSERT_ID();");
    }
}
