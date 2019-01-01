# PUTS_DB_Schema_Usage

*For a quick reference to SQL queries [click here](https://www.w3schools.com/sql/default.asp)

*For a detailed introduction of PUTS DB schema [click here](https://github.com/WyattLiu/ECE496-PUT/blob/database/schema_intro.md)

Recall that the PUTS database consists of four kinds of tables:
- Enum tables
- Reference tables
- Primary tables
- Log tables

#### Enum tables
Recall that Enum tables holds foreign keys frequently referenced by other tables. Say the user want to add a record in a primary table with some unknown~~funny~~ package units, for instance, a **double-decker bus** of **apples**, you may wish to ensure that we already have the `double-decker bus` entry in the `package_units` table by typing
```
> select * from package_units where unit = 'double-decker bus';
```
If no records are found, then it maybe either a typo or a valid new value to insert. In the latter case, insert the `double-decker bus` entry into `package_units` table first,
```
> insert into package_units values ('double-decker bus');
```
and then update the new record.

#### Reference tables
Recall that reference tables assist in speeding up the queries to primary tables, or provide statistics for fields in primary tables. For instance, you can use the `barcodes` table to quickly retrieve the `brand`, `name`, `package unit`, `content unit` and `content quantity` of a barcoded item, without having the user to manually input them. When making queries to reference tables, use `primary key` as the unique identifier to locate a single entry.
```
> select * from barcodes where barcode = '01234567890';
```
The `std_names` table holds the `standarized names` of items displayed on the UI. It is particularly useful when answering questions like **How many eggs do I have?** without needing to look into different egg entries with various fuzzy names like **ProudfarmerSuperOmega3CalciumEnhancedExtraLargeBeautifulEggs**.
```
> select SUM(content_quantity) from grocery_storage where std_name = 'egg';
```

#### Primary tables
Primary tables are where the most of our business logics take place. A newly input item goes to both `purchase_history` and `grocery_storage`. 
```
insert into purchase_history (std_name, vendor, brand, content_quantity, content_unit, is_packaged, package_unit, purchase_date, expiry_date) values ('egg', 'Metro', 'Prestige', '12', 'items', 'T', 'box', '2018-12-22', '2018-12-31');  <-- a uid is automatically assigned to this entry.
```
```
INSERT INTO grocery_storage VALUES (LAST_INSERTED_ID(), 'eggs', 12, 'items', '2018-12-22 06:16:32', '2018-12-22', '2018-12-31', 'unopened'); <-- Use the uid later to trace back to a purchase
```

A **consumption/disposal** action adds a trace in `comsumption_history` and updates `grocery_storage` accordingly. 
```
insert into consumption_history (uid, std_name, consumed_quantity, remaining_quantity, action, time_stamp) VALUES (1, 'egg', 3, 9, 'consumption', '2018-12-23 09:16:32');
```
```
update grocery_storage set content_quantity = 9, status = 'opened', last_updated = '2018-12-23 09:16:32' where uid = 1;
```
We routinely loop through all active items in the `grocery_storage` on a daily basis. On the unfortunate case where something goes past its expiry date, we mark its status as **expired**. The item continues to sit there until further actions are taken.
```
update grocery_storage set status = 'expired', last_updated = '2019-01-01 00:00:00' where uid = 1;
```

Whenever something is **entirely** taken out of the fridge, the action goes to `consumption_history`, the item goes to `void_items` and its `status` is changed accordingly in the `grocery_storage` table. The empty box of eggs is now a phantom trapped in the fridge as we **never delete anything**.
```
insert into consumption_history (uid, std_name, consumed_quantity, remaining_quantity, action, time_stamp) VALUES (1, 'egg', 9, 0, 'disposal', '2019-01-01 09:16:32'); -- What a pity!
```
```
insert into void_items values (1, 'egg', 'disposed', '2019-01-01 09:16:32');
```
```
update grocery_storage set content_quantity = 0, status = 'disposed', last_updated = '2019-01-01 09:16:32' where uid = 1;
```
**Be sure to warn the users if they insist to consume expired stuffs!** 

Being the tables directly facing the users at front-end, its values don't necessarily to be there always, as we don't want to push the user on anything. Yet we must always know something is **not there**. A default value is always better than NULL. If the hassle of setting a field does not fall on the user, then it is on our shoulders. Do ensure we have default values in each enum/reference tables to cover all use cases.

#### Log tables
Recall that log tables holds development-side records like sensor values. They are typically constantly updated by some scripts and tagged with timestamps.
```
insert into temperature_data (time, temperature) values ('2018-12-26 03:04:05', -3.2); <-- an rid will be assigned for this record.
```
