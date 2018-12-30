#!/bin/bash
port=1521
user='puts'
passwd='wearethewinner'
host='culinut.ddns.net'
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
sudo mysql -h $host -P $port -u$user -p$passwd -e "$update_query" $format_args 2> /dev/null;

read_query="use $db; select * from $table where rid = (select MAX(rid) from $table);"
sudo mysql -h $host -P $port -u$user -p$passwd -e "$read_query" $format_args 2> /dev/null;
