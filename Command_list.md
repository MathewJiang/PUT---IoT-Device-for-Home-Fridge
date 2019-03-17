# Comamnd list
## Sensor (readonly)
#### **/show\_fridge_image**
* Binary data of latest image

#### **/show\_latest\_fridge\_image_date**
* Return text of time stamp

#### **/ls\_fridge\_image_history** (Pending)
* Print text of all historical image stored
* This is to support any history look up

#### **/read\_one_old\_fridge\_image** (Pending)
* Binary data of specific image taken in the past
* Same fashion as show_fridge_image

#### **/show_temperature**
* Return in h1 html in C (no C) in decimal i.e. ``<time stamp>, 3.231``

#### **/get_weight**
* Return in kg inside h1

#### **/get_ccs811**
* Return eCO2 and TVOC readings, unit should be ppm

#### **/read_scanner**
* This must be called after turnning on USB
* This blocking and return immediately once get something
* Expect call turn_on_USB, while(scan_mode) {$number = read_scanner && /barcode_lookup/$number } turn_off_USB fashion

## data I/O
#### **/hello_world**
* html, "Hi Android"

#### **/append_testfile/\<data\>**
* Testing function, will be able to append test data / log to a file.
* response ``success_append_testfile``, otherwise failed
* i.e. ``/append_testfile/first, is space is a space of a + sign``

#### **/read_testfile**
* Testing function, print the whole testfile data

#### **/write_testfile/\<data\>**
* Data is overwriten with new data

## Database
#### **/raw_sql/\<data\>**
* by pass all sql command and return data seperated by ; for \n.

## Memcache
#### **/ls_barcode\_cache**
* This will return all stored entry in key value pairs in human readable html
* Some example on it can deal with dangerous char has been shown

#### **/barcode_lookup/\<digits\>**
* This will return name string, if a cache hit, 0.001s.
* And if there is not hit in local cache, it will go online (a few seconds), and if it is not there, error message will return asking for manual update.
* If it found it online, it will update cache, write to disk and response, so next query will save time and quota. Example:
``http://<URL>:8080/barcode_lookup/4901777227071``
returns ``Japan Limited, "coca Cola Clear", Transparent Cola, 280ml``.

## Control

#### **/turn_off_USB**
* This is powering off USB

#### **/turn_on_USB**
* This is powering on USB
