#!/bin/bash
port=1521
user='puts'
passwd='wearethewinner'
host='culinut.ddns.net'
format_args='-N -s -r'

db='putsDB'

if [ $# -ne 1 ]; then
  echo "usage: $0 query_expr"
  exit 1;
fi

sudo mysql -h $host -P $port -u$user -p$passwd -e "use $db; $1" $format_args;
