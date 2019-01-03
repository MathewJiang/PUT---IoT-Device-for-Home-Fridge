#!/usr/bin/perl
use strict;
my $time_stamp = `date`;
chomp $time_stamp;
$time_stamp =~ s/ /_/g;
while(<STDIN>) {
	if($_=~/,(.*),kg,/) {
		print "$1 kg, $time_stamp\n";
		exit;
	}
}

