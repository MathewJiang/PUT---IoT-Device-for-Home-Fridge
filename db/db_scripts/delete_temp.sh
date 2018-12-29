#!/bin/bash
port=1521
user='puts'
passwd='wearethewinner'
host='culinut.ddns.net'
format_args='-N -s -r'

db='putsDB'
table='temperature_data'

if [ $# -ne 1 ]; then
  echo "usage: $0 rid_to_delete"
  exit 1;
fi

if [[ ! $1 =~ ^[0-9]+$ ]] ; then
  echo "invalid rid $1: must be a integer"
  exit 1;
fi

delete_query="use $db; delete from $table where rid = $1;"
sudo mysql -h $host -P $port -u$user -p$passwd -e "$delete_query" $format_args 2> /dev/null;
