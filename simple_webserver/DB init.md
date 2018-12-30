## DB init

### Install ENV
``sudo apt-get install mysql-server python-mysqldb``

* If failed, try apt-get update

### Import schema with test data
``sudo mysql -t < putsDB.sql``

### Run offline script in db/db_scripts