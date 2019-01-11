package com.example.zhepingjiang.db;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * DB Access Unit Tests
 */
public class DBAccessTest {

    @Test
    public void testGetFullQuery_happyPath() {
        final String query = "select * from blah;";
        final String expected = DBAccess.USE_DB_COMMAND + query;

        final String actual = DBAccess.GetFullQuery(query);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetFullQuery_emptyQuery_happyPath() {
        final String query = "";
        final String expected = DBAccess.USE_DB_COMMAND;

        final String actual = DBAccess.GetFullQuery(query);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetQueryLink_happyPath() {
        final String query = "select * from blah;";
        final String expected = DBAccess.ROOT_URL + DBAccess.USE_DB_COMMAND + query;

        final String actual = DBAccess.GetQueryLink(query);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetQueryLink_emptyQuery_happyPath() {
        final String query = "";
        final String expected = DBAccess.ROOT_URL + DBAccess.USE_DB_COMMAND + query;

        final String actual = DBAccess.GetQueryLink(query);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetLastInsertIdQuery_happyPath() {
        final String expected = "use putsDB;select LAST_INSERT_ID();";

        final String actual = DBAccess.GetLastInsertIdQuery();

        assertEquals(expected, actual);
    }
}