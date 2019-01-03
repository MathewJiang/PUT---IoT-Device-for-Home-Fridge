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