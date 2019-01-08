package com.example.zhepingjiang.db;

public interface putsDBEntry {

    // Returns the DB table name of current class
    String getTableName();

    // Returns the SQL query to insert the record.
    String getInsertQuery();

    // Returns the SQL query to insert the record if not exist.
    String getUpsertQuery();

    // Returns the SQL query to delete the record.
    String getDeleteQuery();

    // Returns the SQL query to find the record by some keys.
    String getSelectQuery();
}
