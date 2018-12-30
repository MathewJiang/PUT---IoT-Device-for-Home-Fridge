#!/bin/bash
format_args='-N -s -r'

db='putsDB'
query="use $db; show tables;"

sudo mysql -e "$query" $format_args;
