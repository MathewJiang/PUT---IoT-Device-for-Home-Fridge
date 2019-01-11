# Java Business Objects in PUTS

* For a quick reference to PUTS DB schema [click here](https://github.com/WyattLiu/ECE496-PUT/blob/database/db/putsDB.sql)

Business Objects interconnects the persistence layer(database) and the business layer(android app) logics. It abstracts away the most efforts (insert, query, delete, etc) making transactions with the database and presenting data to the application. In the PUTS project, a complete set of Java Business Objects(BOs, same below) are deployed in the android app, representing every table in the putsDB schema. Currently the BOs mainly do two things:

- Generate SQL queries from BOs
- Convert SQL responses into BOs

#### Business-layer representations of putsDB schema
Business Objects present and process information from the putsDB into the needs of the business layer. Each BO consists of exactly the same number of fields corresponding to the putsDB schema. For example, the `purchase_history` table in putsDB may look like:
```
CREATE TABLE purchase_history (
    uid                     INT             NOT NULL AUTO_INCREMENT,
    std_name                VARCHAR(30)     NOT NULL,
    vendor                  VARCHAR(30)     NOT NULL,
    brand                   VARCHAR(30)     NOT NULL,
    content_quantity        INT             NOT NULL,
    content_unit            VARCHAR(30)     NOT NULL,
    is_packaged             CHAR(1)         NOT NULL CHECK (is_packaged IN ('T', 'F')), -- T/F
    package_unit            VARCHAR(30)     NOT NULL,
    purchase_date           DATE            NOT NULL,
    expiry_date             DATE            NOT NULL,
    FOREIGN KEY (std_name)                  REFERENCES std_names (std_name)     ON DELETE CASCADE,
    FOREIGN KEY (vendor)                    REFERENCES vendors (vendor_name)    ON DELETE CASCADE,
    FOREIGN KEY (brand)                     REFERENCES brands (brand_name)      ON DELETE CASCADE,
    FOREIGN KEY (content_unit)              REFERENCES content_units (unit)     ON DELETE CASCADE,
    FOREIGN KEY (package_unit)              REFERENCES package_units (unit)     ON DELETE CASCADE,
    PRIMARY KEY (uid)
);
```
The corresponding Business Layer Representation looks like:
```
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
    ...
```
You may notice how the `T/F` char(1) representation of `is_packaged` field is processed into a `boolean` field in the BO. In addition, every field referencing a foreign table value is processed into ***another BO representing that foreign table***. This allows us to ***recursively*** build SQL queries on top of each other.

#### Generating SQL queries from BOs
Business Objects use the values in their fields to build SQL entries. To generate an `insert` query is as simple as
```
// Construct the BO of the table. Choose the constructor based on information you have.
final BO bo = new BO(...);
// Specify all necessary fields, or leave to use default values.
bo.setUID(123);
// Generate the insert query.
final String insertQuery = bo.getInsertQuery();
```
That's it. To pack the query into an actionable query URL, use the `DBAccess` utility functions:
```
final String queryURL = DBAccess.GetQueryLink(insertQuery);
```
The generated query first `upserts`(update or insert) every foreign entries in the BO to ensure they are actually there. It then inserts the entry into the target table. Sounds like a breeze, isn't it?

Some operations like `delete`/`select` doesn't make use a full BO, as they only need the primary key (e.g. `UID`) to do the job. This case we also have some **static methods** to quickly get you the query you need. For example:
```
final int purchaseHistoryUID = 123;
// Generate a select query based on UID.
final String selectQuery = PurchaseHistory.GetSelectQueryStatic(123);
// Generate a delete query based on UID.
final String deleteQuery = PurchaseHistory.GetDeleteQueryStatic(123);
```
Feeling like dumping the entire table? Just do
```
final String dumpTableQuery = PurchaseHistory.GetSelectAllQueryStatic();
```
As always, feel free to use the **object member functions** to to the same:
```
// Say you have already constructed a BO above.
final PurchaseHistory phObj = new PurchaseHistory(...);
phObj.setUID(123);
// Do some work with it.
...
// Now you want to delete it in DB.
final String deleteQuery = phObj.getDeleteQuery();
// Or dump the entire table from DB.
final String dumpTableQuery = phObj.getSelectAllQuery();
```

#### Converting SQL response into BOs
Another important aspect is fetching relevant information from DB. The Business Objects provide functionalities to efficiently convert the SQL responses into the business-layer representations to get you on work.

Currently the mySQL server yields SQL responses in `HTML Table` format like:
```
> select * from actions;
<TABLE BORDER=1>
    <TR>
        <TH>user_action</TH>
    </TR>
    <TR>
        <TD>consumption</TD>
    </TR>
    <TR>
        <TD>disposal</TD>
    </TR>
    <TR>
        <TD>default</TD>
    </TR>
</TABLE>
```
This format allows us to efficiently and reliably boil down the response string into BOs. Simply take advantage of the ***FromHTMLTableStr*** static method in each BO class:
```
final String htmlTableStr = "<TABLE BORDER=1><TR><TH>user_action</TH></TR><TR><TD>consumption</TD></TR><TR><TD>default</TD></TR><TR><TD>disposal</TD></TR></TABLE>";
final Set<Actions> convertedObjs = Actions.FromHTMLTableStr(htmlTableStr);
```
You have the Set containing 4 converted Actions BOs.

Note that the parser can **"intelligently"** combine results from multiple tables in the string, filter out schemas that doesn't belong to the table, and even cherrypick the corrupted entries that lack columns. All these features are backed with **"rigorous"** unit/integration test coverages for correctness and robustness. Play with it and you will find out more.

**Good Luck!**


