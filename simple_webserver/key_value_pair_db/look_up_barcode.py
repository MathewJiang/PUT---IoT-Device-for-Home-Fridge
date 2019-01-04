#!/usr/bin/python3

import urllib3
from urllib.parse import urlparse
import httplib2 as http
import json

import sys

for line in sys.stdin:
    barcode = line.rstrip()

headers = {
  'Content-Type': 'application/json',
  'Accept': 'application/json',
}
ch = http.Http()

lookup = urlparse('https://api.upcitemdb.com/prod/trial/lookup?upc='+barcode)
resp, content = ch.request(lookup.geturl(), 'GET', '', headers)
data = json.loads(content.decode('utf-8'))

word = 'items'
key = 'title'
if word in data:
	if len(data[word]):
		if key in data[word][0]:
			print (((data[word])[0])[key])
	else:
		print ('Not in database')
