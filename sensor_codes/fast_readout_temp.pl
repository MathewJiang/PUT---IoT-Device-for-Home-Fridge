#!/usr/bin/perl
use strict;
use warnings;

# prolog

my $probe_dir = `readlink -f /sys/bus/w1/devices/28*`;
chomp $probe_dir;
my $file = `readlink -f $probe_dir/w1_slave`;
chomp $file;
if(!$file =~ /wl_slave/) {
	print "Did not find the probe $file\n";
	exit;
}
#print "Proble file is located at $file\n";
open(my $fh,'<',$file);
my $valid = 0;
my $temp;
while(<$fh>) {
	if($_=~/YES/) {
		$valid = 1;
		next;
	}
	if($valid) {
#		print $_;
		$valid = 0;
		$temp = $_;
		last;
	}

}
chomp $temp;
if($temp=~/.*=(.*)/) {
	my $temp_in_C = $1/1000;
	print "Temperature is $temp_in_C\n";
}

