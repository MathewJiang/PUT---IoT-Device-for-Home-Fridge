#!/bin/bash
continuous_mode_flag=1
while read -r barcode < /dev/ttyACM0; do
  echo $barcode
  if [[ $# -le 1 ]]  && [[ $1 -ne 1 ]]; then
    exit 0;
  fi
done
