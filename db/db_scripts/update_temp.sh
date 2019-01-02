#!/bin/bash
format_args='-N -s -r'

db='putsDB'
table='temperature_data'

if [ $# -ne 1 ]; then
  echo "usage: $0 temp_value"
  exit 1;
fi

if [[ ! $1 =~ ^[-+]?[0-9]+\.?[0-9]*$ ]] ; then
  echo "invalid temp value $1: must be a float"
  exit 1;
fi

time_stamp=`date '+%Y-%m-%d %H:%M:%S'`

update_query="use $db; insert into $table (time, temperature) values ('$time_stamp', $1);"
sudo mysql -e "$update_query" $format_args;

read_query="use $db; select * from $table where rid = (select MAX(rid) from $table);"
sudo mysql -e "$read_query" $format_args;
