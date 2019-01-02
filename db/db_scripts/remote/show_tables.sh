#!/bin/bash
port=1521
user='puts'
passwd='wearethewinner'
host='culinut.ddns.net'
format_args='-N -s -r'

db='putsDB'
query="use $db; show tables;"

sudo mysql -h $host -P $port -u$user -p$passwd -e "$query" $format_args 2> /dev/null;
