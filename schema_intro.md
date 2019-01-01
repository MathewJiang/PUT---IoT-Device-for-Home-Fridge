# SQL Schema

The PUTS database consists of four kinds of tables:
- Enum tables
- Reference tables
- Primary tables
- Log tables

#### Enum tables

***Enum tables*** hold pre-defined enumeration entries like `item_status`, `user_action` and `units`. These tables are typically **single column** and frequently referenced by other tables. In PUTS DB, such tables are:
- `package_units` (e.g. box)
- `content_units` (e.g. ml)
- `statuses` (e.g. expired)
- `actions` (e.g. consumption)

#### Reference tables
***Reference tables*** hold secondary audit records for primary tables, such as `brand`, `vendor` and `barcodes`. These tables are typically advisory for speedy entry purpose, and provides statistics on various aspects. Additionally, these table columns are referenced as foreign keys in primary tables to ensure integrity. In PUTS DB, such tables are:
- `brands` (e.g. Dole)
- `vendors` (e.g. Loblaws)
- `categories` (e.g. Dairy)
- `std_names` (e.g. 3.25% Milk)
- `barcodes`

#### Primary tables
***Primary tables*** are lively updated tables to hold user grocery records. Entries in these tables are uniquely indexed by auto-incrementing sequence numbers like `uid` and `opid`. In PUTS DB, such tables are:
- `purchase_history`
- `consumption_history`
- `grocery_storage`
- `void_items`

The primary tables relate each other as follows: **purchase_history** table assigns a **uid** to each **package** of grocery item, which serves as a single entry (e.g. One **box** of milk, 8 apples in a **bag**, etc.). These entries are created when user scans in a barcoded package, or manually creates a purchase record. This **uid** is used across primary tables to uniquely identify the entry. At the end of day, **anything consumed/disposed/expired must trace back to a purchase in the past.**

**consuption_history** and **void_items** tracks changes of the grocery after purchase. Whenever a portion or whole of an entry is acted upon (i.e. **consumed/disposed**) by the user, the action is recorded in the **consumption_history** table, tagged by a timestamp. When an entry is no longer active (**entirely consumed/disposed**), it is then added in the **void_items** table.

The **grocery_storage** table, on the other hand, ***comprehensively*** tracks the items **existing** in the fridge. Concisely, it is basically **purchase minus consumption minus void items**. You might want to update this table **as a result** of modifications in other primary tables.

#### Log tables
**Log tables** holds development-side records like sensor values. Currently we have only one log table: `temperature_data`.
