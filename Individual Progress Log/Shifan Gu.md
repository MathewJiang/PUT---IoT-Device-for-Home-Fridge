# Shifan Gu

## Dec. 27 2018
### Wrapper scripts to access DB remotely
* Spun up MySQL server on local Raspberry Pi.
* Configured local network for remote access.

The putsDB can be accessed either:
- Locally
  - Install [MySQL environment](http://raspberrywebserver.com/sql-databases/using-mysql-on-a-raspberry-pi.html) on Linux 
  - Import [putsDB schema](https://github.com/WyattLiu/ECE496-PUT/blob/database/db/putsDB.sql) into local DB by executing
    ```
    % sudo mysql -t < putsDB.sql
    ```
  - If you don't want sample data to be imported, comment out the last line in `putsDB.sql`:
    ```
    -- source putsDB_testData.sql; 
    ```

or
- Via remote DB access [scripts](https://github.com/WyattLiu/ECE496-PUT/tree/database/db/db_scripts)
  - Install [MySQL environment](http://raspberrywebserver.com/sql-databases/using-mysql-on-a-raspberry-pi.html) on Linux 
  - Execute one of the shell scripts under `/db_scripts` directory. Example:
    ```
    % sudo ./update_temp.sh -3.57 
    ```
    will insert a new record in `temperature_data` table looking like:
    ```
    rid time                temperature 
     10 2018-12-29 17:30:46       -3.57 
    ```
    with record id `10`,  temperature value `-3.57` and `live timestamp`.

## Dec. 25 2018
### MySQL Server deployment on Raspberry Pi
- Spun up local MySQL server and imported the schema for the project DB. The server currently only listens to `localhost` requests on port `3306`. It can be configured to accept requests from remote hosts if needed.
- Created a dedicated user for the PUTS Project.

## Dec. 23 2018
### SQL Schema For Puts DB
- Designed the [database schema](https://github.com/WyattLiu/ECE496-PUT/blob/database/db/putsDB.sql) for the project. It is consisted of multiple small-yet-concise tables for maximum granularity and thus minimum redundancy.
- Tested with [mock data](https://github.com/WyattLiu/ECE496-PUT/blob/database/db/putsDB_testData.sql) for correctness of `data integrity constraints` and `foreign key referral constraints`

## Dec. 22 2018
### Getting Started with Raspberry Pi
- Purchased a `Raspberry Pi 3B+` model for my personal development work. It can be used as a `persistent server` for some project infrastructures like `DB` and `alert service`.
- Started going through the tutorials for various components like sensors and servo motors. Will be useful if we want to build up extra hardware functionality.
