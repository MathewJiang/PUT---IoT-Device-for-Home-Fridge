#!/bin/bash

format_args='-N -s -r'

db='putsDB'
table='temperature_data';
query="use $db; select temperature from $table where rid = (select MAX(rid) from $table);"

sudo mysql -e "$query" $format_args;
