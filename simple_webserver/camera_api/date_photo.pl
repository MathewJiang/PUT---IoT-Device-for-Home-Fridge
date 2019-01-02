#!/usr/bin/perl
use strict;
use warnings;

my $time = `date`;
$time =~ s/ /_/g;
print $time;
