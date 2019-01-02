#!/bin/bash
format_args='-N -s -r'

db='putsDB'

if [ $# -ne 1 ]; then
  echo "usage: $0 query_expr"
  exit 1;
fi

sudo mysql -e "use $db; $1" $format_args;
