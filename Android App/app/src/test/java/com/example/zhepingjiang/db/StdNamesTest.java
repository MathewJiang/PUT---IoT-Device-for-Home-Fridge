package com.example.zhepingjiang.db;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Std Names Table Unit Tests.
 */
public class StdNamesTest {

    @Test
    public void testGetTableName_happyPath() {
        final String expectedTableName = "std_names";

        final String actualTableName = new StdNames("somename").getTableName();

        assertEquals(expectedTableName, actualTableName);
    }

    @Test
    public void testBuildStdNames_defaultConstr_withDefaultCategory() {
        final Categories expectedCategory = new Categories();

        final StdNames defaultObj = new StdNames("somename");

        assertEquals(expectedCategory.getCategory(), defaultObj.getCategory().getCategory());
    }

    @Test
    public void testBuildStdNames_allArgsConstr_withSpecifiedCategory() {
        final Categories expectedCategory = new Categories("someCategory");

        final StdNames allArgsObj = new StdNames("somename", expectedCategory);

        assertEquals(expectedCategory.getCategory(), allArgsObj.getCategory().getCategory());
    }

    @Test
    public void testGetInsertQuery_uncategorizedItem_happyPath() {
        final Categories expectedCategory = new Categories();
        final String expectedQuery = expectedCategory.getUpsertQuery() +
                "insert into std_names values ('Coco-cola','uncategorized');";

        final String actualQuery = new StdNames("Coco-cola").getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetInsertQuery_categorizedItem_happyPath() {
        final Categories expectedCategory = new Categories("drink");
        final String expectedQuery = expectedCategory.getUpsertQuery() +
                "insert into std_names values ('Coco-cola','drink');";

        final String actualQuery = new StdNames("Coco-cola", expectedCategory).getInsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_uncategorizedItem_happyPath() {
        final Categories expectedCategory = new Categories();
        final String expectedQuery = expectedCategory.getUpsertQuery() +
                "insert ignore into std_names values ('Coco-cola','uncategorized');";

        final String actualQuery = new StdNames("Coco-cola").getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetUpsertQuery_categorizedItem_happyPath() {
        final Categories expectedCategory = new Categories("drink");
        final String expectedQuery = expectedCategory.getUpsertQuery() +
                "insert ignore into std_names values ('Coco-cola','drink');";

        final String actualQuery = new StdNames("Coco-cola", expectedCategory).getUpsertQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQuery_happyPath() {
        final String expectedQuery = "delete from std_names where std_name = 'Coco-cola';";

        final String actualQuery = new StdNames("Coco-cola").getDeleteQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQuery_happyPath() {
        final String expectedQuery = "select * from std_names where std_name = 'Coco-cola';";

        final String actualQuery = new StdNames("Coco-cola").getSelectQuery();

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetDeleteQueryStatic_happyPath() {
        final String expectedQuery = "delete from std_names where std_name = 'Orange juice';";

        final String actualQuery = StdNames.GetDeleteQueryStatic("Orange juice");

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectQueryStatic_happyPath() {
        final String expectedQuery = "select * from std_names where std_name = 'Orange juice';";

        final String actualQuery = StdNames.GetSelectQueryStatic("Orange juice");

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetSelectAllQueryStatic_happyPath() {
        final String expectedQuery = "select * from std_names;";

        final String actualQuery = StdNames.GetSelectAllQueryStatic();

        assertEquals(expectedQuery, actualQuery);
    }
}