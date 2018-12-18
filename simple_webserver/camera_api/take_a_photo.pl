#!/usr/bin/perl
use strict;
use warnings;

`raspistill -vf -hf -w 1920 -h 1080 -q 75 -o /home/pi/proj/gited/ECE496-PUT/simple_webserver/camera_api/latest.jpg`;

